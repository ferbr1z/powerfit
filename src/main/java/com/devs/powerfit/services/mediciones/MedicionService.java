package com.devs.powerfit.services.mediciones;

import com.devs.powerfit.beans.clientes.ClienteBean;
import com.devs.powerfit.beans.mediciones.MedicionBean;
import com.devs.powerfit.daos.mediciones.MedicionDao;
import com.devs.powerfit.dtos.clientes.ClienteDto;
import com.devs.powerfit.dtos.mediciones.MedicionDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.interfaces.mediciones.IMedicionService;
import com.devs.powerfit.services.clientes.ClienteService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.clienteMappers.ClienteMapper;
import com.devs.powerfit.utils.mappers.medicionMappers.MedicionMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MedicionService implements IMedicionService {
    private MedicionDao medicionDao;
    private ClienteService clienteService;
    private MedicionMapper mapper;
    private ClienteMapper clienteMapper;
    private CacheManager cacheManager;
    @Autowired
    public MedicionService(MedicionDao medicionDao, ClienteService clienteService,
                           MedicionMapper mapper, ClienteMapper clienteMapper,
                           CacheManager cacheManager){
        this.medicionDao = medicionDao;
        this.clienteService = clienteService;
        this.mapper = mapper;
        this.cacheManager = cacheManager;
        this.clienteMapper = clienteMapper;
    }
    @Override
    public MedicionDto create(MedicionDto medicionDto) {
        // Verificar si los campos obligatorios no están incompletos
        if (medicionDto.getClienteID() == null || medicionDto.getFecha() == null) {
            throw new BadRequestException("El campo clienteID y fecha son obligatorios para crear una nueva medición");
        }

        // Verificar si el cliente existe
        ClienteDto clienteDto = clienteService.getById(medicionDto.getClienteID());

        // Crear una instancia de Medicion desde MedicionDto
        MedicionBean medicion = new MedicionBean();
        medicion.setCliente(clienteMapper.toBean(clienteDto));
        medicion.setFecha(medicionDto.getFecha());
        medicion.setPeso((medicionDto.getPeso()));
        medicion.setAltura(medicionDto.getAltura());
        medicion.setImc(medicionDto.getImc());
        medicion.setCirBrazo(medicionDto.getCirBrazo());
        medicion.setCirPiernas(medicionDto.getCirPiernas());
        medicion.setCirCintura(medicionDto.getCirCintura());
        medicion.setCirPecho(medicionDto.getCirPecho());
        medicion.setActive(true);

        // Guardar la medicion en la base de datos
        MedicionBean savedMedicion = medicionDao.save(medicion);

        // Retornar el MedicionDao creado
        return mapper.toDto(savedMedicion);
    }

    @Cacheable(cacheNames = "IS::api_mediciones", key = "'medicion_'+#id")
    @Override
    public MedicionDto getById(Long id) {
        var medicionOptional = medicionDao.findById(id);
        if(medicionOptional.isPresent()){
            var medicionBean = medicionOptional.get();
            return mapper.toDto(medicionBean);
        }
        throw new NotFoundException("Medición no encontrada");
    }

    @Override
    public PageResponse<MedicionDto> getAll(int page) {
        var pag = PageRequest.of(page -1, Setting.PAGE_SIZE);
        var mediciones = medicionDao.findAllByActiveTrue(pag);

        if(mediciones.isEmpty()){
            throw new NotFoundException("No hay mediciones en la lista");
        }

        var medicionesDto = mediciones.map(medicion -> mapper.toDto(medicion));

        // Cachear manualmente cada medicion en Redis
        for(MedicionDto medicionDto : medicionesDto){
            String cacheName = "IS::api_mediciones";
            String key = "medicion_" + medicionDto.getId();
            Cache cache = cacheManager.getCache(cacheName);

            // Verificar si la actividad ya está en la caché
            Cache.ValueWrapper valueWrapper = cache.get(key);

            if (valueWrapper == null){
                // Si no está en la caché, cachearla
                cache.put(key, medicionDto);
            }
        }
        var pageResponse = new PageResponse<MedicionDto>(
                medicionesDto.getContent(),
                medicionesDto.getTotalPages(),
                medicionesDto.getTotalElements(),
                medicionesDto.getNumber() + 1
        );
        return pageResponse;
    }
    @CachePut(cacheNames = "IS::api_mediciones", key = "'medicion_'+#id")
    @Override
    public MedicionDto update(Long id, MedicionDto medicionDto) {
        var medicionOptional = medicionDao.findByClienteIdAndActiveTrue(id);
        if(medicionOptional.isPresent()){
            var medicionBean = medicionOptional.get();

            // Actualizar los campos de la medicion con los valores del DTO
            if(medicionDto.getFecha() != null){
                medicionBean.setFecha(medicionDto.getFecha());
            }

            if (medicionDto.getPeso() != null) {
                medicionBean.setPeso(medicionDto.getPeso());
            }
            if (medicionDto.getAltura() != null) {
                medicionBean.setAltura(medicionDto.getAltura());
            }
            if (medicionDto.getImc() != null) {
                medicionBean.setImc(medicionDto.getImc());
            }
            if (medicionDto.getCirBrazo() != null) {
                medicionBean.setCirBrazo(medicionDto.getCirBrazo());
            }
            if (medicionDto.getCirPiernas() != null) {
                medicionBean.setCirPiernas(medicionDto.getCirPiernas());
            }
            if (medicionDto.getCirCintura() != null) {
                medicionBean.setCirCintura(medicionDto.getCirCintura());
            }
            if (medicionDto.getCirPecho() != null) {
                medicionBean.setCirPecho(medicionDto.getCirPecho());
            }

            // Verificar si se proporciona el ID del cliente para actualizar el cliente asociado
            if(medicionDto.getClienteID() != null){
                // Obtener el cliente asociado a la suscripción
                ClienteDto clienteDto = clienteService.getById(medicionDto.getClienteID());

                // Asignar el cliente actualizado a la suscripción
                medicionBean.setCliente(clienteMapper.toBean(clienteDto));
            }

            medicionDao.save(medicionBean);

            return  mapper.toDto(medicionBean);
        }
        throw new NotFoundException("Medición no encontrada");
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public PageResponse<MedicionDto> searchByNombreCliente(String nombre, int page) {
        return null;
    }
}
