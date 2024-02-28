package com.devs.powerfit.services.clientes;

import com.devs.powerfit.beans.clientes.ClienteBean;
import com.devs.powerfit.daos.clientes.ClienteDao;
import com.devs.powerfit.dtos.actividades.ActividadDto;
import com.devs.powerfit.dtos.clientes.ClienteDto;
import com.devs.powerfit.utils.responses.PageResponse;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.clientes.IClienteService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.clienteMappers.ClienteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class ClienteService implements IClienteService {

    private ClienteDao clienteDao;
    private ClienteMapper mapper;
    private CacheManager cacheManager;
    @Autowired
    public ClienteService(ClienteDao clienteDao, ClienteMapper mapper, CacheManager cacheManager) {
        this.clienteDao = clienteDao;
        this.mapper = mapper;
        this.cacheManager = cacheManager;
    }


    @Override
    public ClienteDto create(ClienteDto clienteDto) {
        // Verificar si los campos obligatorios no están incompletos
        if (clienteDto.getNombre() == null || clienteDto.getCedula() == null) {
            throw new BadRequestException("Los campos nombre y cedula son obligatorios para crear un nuevo cliente");
        }

        // Verificar si ya existe un cliente con la misma cédula
        Optional<ClienteBean> existingClient = clienteDao.findByCedula(clienteDto.getCedula());

        if (existingClient.isPresent()) {
            ClienteBean cliente = existingClient.get();
            if (cliente.isActive()) {
                throw new BadRequestException("Ya existe un cliente activo con la misma cédula");
            } else {
                // Si el cliente existe pero está inactivo, activarlo
                cliente.setActive(true);
                clienteDao.save(cliente);
                return mapper.toDto(cliente);
            }
        } else {
            // Si no existe un cliente activo con la misma cédula, crear un nuevo cliente
            ClienteBean cliente = mapper.toBean(clienteDto);
            cliente.setActive(true);
            cliente.setFechaRegistro(LocalDate.now());
            clienteDao.save(cliente);
            return mapper.toDto(cliente);
        }
    }



    @Cacheable(cacheNames = "IS::api_clientes", key = "'cliente_'+#id")
    @Override
    public ClienteDto getById(Long id) {
        var cliente = clienteDao.findByIdAndActiveTrue(id);
        if (cliente.isPresent()) {
            return mapper.toDto(cliente.get());
        }
        throw new NotFoundException("Cliente no encontrado");

    }

    @Override
    public PageResponse<ClienteDto> getAll(int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var clientes = clienteDao.findAllByActiveTrue(pag);

        if (clientes.isEmpty()) {
            throw new NotFoundException("No hay clientes en la lista");
        }

        var clientesDto = clientes.map(cliente -> mapper.toDto(cliente));
        // Cachear manualmente cada cliente en Redis
        for (ClienteDto clienteDto : clientesDto) {
            String cacheName = "sd::api_clientes";
            String key = "cliente_" + clienteDto.getId();
            Cache cache = cacheManager.getCache(cacheName);

            // Verificar si la actividad ya está en la caché
            Cache.ValueWrapper valueWrapper = cache.get(key);

            if (valueWrapper == null) {
                // Si no está en la caché, cachearla
                cache.put(key, clienteDto);
            }
        }
        var pageResponse = new PageResponse<ClienteDto>(clientesDto.getContent(),
                clientesDto.getTotalPages(),
                clientesDto.getTotalElements(),
                clientesDto.getNumber() + 1);
        return pageResponse;
    }
    @CachePut(cacheNames = "IS::api_clientes", key = "'cliente_'+#id")
    @Override
    public ClienteDto update(Long id, ClienteDto clienteDto) {
        var cliente = clienteDao.findByIdAndActiveTrue(id);
        if (cliente.isPresent()) {
            var clienteBean = cliente.get();

            if (clienteDto.getNombre() != null) clienteBean.setNombre(clienteDto.getNombre());
            if (clienteDto.getCedula() != null) clienteBean.setCedula(clienteDto.getCedula());
            if (clienteDto.getRuc() != null) clienteBean.setRuc(clienteDto.getRuc());
            if (clienteDto.getTelefono() != null) clienteBean.setTelefono(clienteDto.getTelefono());
            if (clienteDto.getEmail() != null) clienteBean.setEmail(clienteDto.getEmail());
            if (clienteDto.getDireccion() != null) clienteBean.setDireccion(clienteDto.getDireccion());

            clienteDao.save(clienteBean);

            return mapper.toDto(clienteBean);
        }
        throw new NotFoundException("Cliente no encontrado");
    }
    @CacheEvict(cacheNames = "IS::api_clientes", key = "'cliente_'+#id")
    @Override
    public boolean delete(Long id) {
        var cliente = clienteDao.findByIdAndActiveTrue(id);
        if (cliente.isPresent()) {
            var clienteBean = cliente.get();
            clienteBean.setActive(false);
            clienteDao.save(clienteBean);
            return true;
        }
        throw new NotFoundException("Cliente no encontrado");
    }
    @Override
    public PageResponse<ClienteDto> searchByNombre(String nombre, int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var clientes = clienteDao.findByNombreContainingIgnoreCaseAndActiveIsTrue(pag, nombre);

        if (clientes.isEmpty()) {
            throw new NotFoundException("No hay clientes en la lista");
        }

        var clientesDto = clientes.map(cliente -> mapper.toDto(cliente));
        // Cachear manualmente cada cliente en Redis
        for (ClienteDto clienteDto : clientesDto) {
            String cacheName = "sd::api_clientes";
            String key = "cliente_" + clienteDto.getId();
            Cache cache = cacheManager.getCache(cacheName);

            // Verificar si la actividad ya está en la caché
            Cache.ValueWrapper valueWrapper = cache.get(key);

            if (valueWrapper == null) {
                // Si no está en la caché, cachearla
                cache.put(key, clienteDto);
            }
        }
        var pageResponse = new PageResponse<ClienteDto>(
                clientesDto.getContent(),
                clientesDto.getTotalPages(),
                clientesDto.getTotalElements(),
                clientesDto.getNumber() + 1);
        return pageResponse;
    }


    @Override
    public PageResponse<ClienteDto> searchByCi(String ci, int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var clientes = clienteDao.findByCedulaAndActiveIsTrue(pag, ci);

        if(clientes.isEmpty()){
            throw new NotFoundException("No hay clientes con esa cedula");
        }

        var clientesDto = clientes.map(cliente -> mapper.toDto(cliente));
        // Cachear manualmente cada cliente en Redis
        for (ClienteDto clienteDto : clientesDto) {
            String cacheName = "sd::api_clientes";
            String key = "cliente_" + clienteDto.getId();
            Cache cache = cacheManager.getCache(cacheName);

            // Verificar si la actividad ya está en la caché
            Cache.ValueWrapper valueWrapper = cache.get(key);

            if (valueWrapper == null) {
                // Si no está en la caché, cachearla
                cache.put(key, clienteDto);
            }
        }
        var pageResponse = new PageResponse<ClienteDto>(
                clientesDto.getContent(),
                clientesDto.getTotalPages(),
                clientesDto.getTotalElements(),
                clientesDto.getNumber() + 1);

        return pageResponse;
    }
}
