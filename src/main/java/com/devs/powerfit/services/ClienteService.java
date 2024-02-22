package com.devs.powerfit.services;

import com.devs.powerfit.beans.ClienteBean;
import com.devs.powerfit.daos.ClienteDao;
import com.devs.powerfit.dtos.ClienteDto;
import com.devs.powerfit.dtos.PageResponse;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.IClienteService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.clienteMappers.ClienteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ClienteService implements IClienteService {

    private ClienteDao clienteDao;
    private ClienteMapper mapper;
    @Autowired
    public ClienteService(ClienteDao clienteDao, ClienteMapper mapper) {
        this.clienteDao = clienteDao;
        this.mapper = mapper;
    }


    @Override
    public ClienteDto create(ClienteDto clienteDto) {
        // Verificar si ya existe un cliente con los mismos campos únicos (activo o inactivo)
        Optional<ClienteBean> existingClient = clienteDao.findByRucAndCedula(clienteDto.getRuc(), clienteDto.getCedula());

        if (existingClient.isPresent()) {
            // Si el cliente ya existe, actualizar el estado activo a true y guardarlo
            ClienteBean clienteExistente = existingClient.get();
            clienteExistente.setActive(true);
            clienteDao.save(clienteExistente);
            return mapper.toDto(clienteExistente);
        } else {
            // Si el cliente no existe, crear un nuevo cliente
            var cliente = mapper.toBean(clienteDto);
            cliente.setActive(true);
            cliente.setFechaRegistro(LocalDate.now());
            clienteDao.save(cliente);
            return mapper.toDto(cliente);
        }
    }

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
        var pageResponse = new PageResponse<ClienteDto>(clientesDto.getContent(),
                clientesDto.getTotalPages(),
                clientesDto.getTotalElements(),
                clientesDto.getNumber() + 1);
        return pageResponse;
    }

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
        var clientes = clienteDao.findByNombreAndActiveIsTrue(pag, nombre);

        if (clientes.isEmpty()) {
            throw new NotFoundException("No hay clientes en la lista");
        }

        var clientesDto = clientes.map(cliente -> mapper.toDto(cliente));
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
        var pageResponse = new PageResponse<ClienteDto>(
                clientesDto.getContent(),
                clientesDto.getTotalPages(),
                clientesDto.getTotalElements(),
                clientesDto.getNumber() + 1);

        return pageResponse;
    }
}
