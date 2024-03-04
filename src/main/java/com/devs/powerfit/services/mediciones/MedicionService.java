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
        return null;
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
