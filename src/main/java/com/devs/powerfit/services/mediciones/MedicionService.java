package com.devs.powerfit.services.mediciones;

import com.devs.powerfit.beans.clientes.ClienteBean;
import com.devs.powerfit.beans.mediciones.MedicionBean;
import com.devs.powerfit.daos.mediciones.MedicionDao;
import com.devs.powerfit.dtos.clientes.ClienteDto;
import com.devs.powerfit.dtos.mediciones.MedicionDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.interfaces.mediciones.IMedicionService;
import com.devs.powerfit.services.clientes.ClienteService;
import com.devs.powerfit.utils.mappers.clienteMappers.ClienteMapper;
import com.devs.powerfit.utils.mappers.medicionMappers.MedicionMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
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
    public MedicionService(MedicionDao medicionDao, MedicionMapper mapper, CacheManager cacheManager){
        this.medicionDao = medicionDao;
        this.mapper = mapper;
        this.cacheManager = cacheManager;
    }
    @Override
    public MedicionDto create(MedicionDto medicionDto) {
        // Verificar si los campos obligatorios no están incompletos
        if (medicionDto.getClienteID() == null || medicionDto.getFecha() == null) {
            throw new BadRequestException("El campo clienteID fecha son obligatorios para crear una nueva medición");
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

    @Override
    public MedicionDto getById(Long id) {
        return null;
    }

    @Override
    public PageResponse<MedicionDto> getAll(int page) {
        return null;
    }

    @Override
    public MedicionDto update(Long id, MedicionDto medicionDto) {
        return null;
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
