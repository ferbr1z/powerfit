package com.devs.powerfit.services.cajas;

import com.devs.powerfit.dtos.cajas.SesionCajaDto;
import com.devs.powerfit.interfaces.cajas.ISesionCajaService;
import com.devs.powerfit.utils.responses.PageResponse;

public class SesionCajaService implements ISesionCajaService {
    @Override
    public SesionCajaDto create(SesionCajaDto sesionCajaDto) {
        return null;
    }

    @Override
    public SesionCajaDto getById(Long id) {
        return null;
    }

    @Override
    public PageResponse<SesionCajaDto> getAll(int page) {
        return null;
    }

    @Override
    public SesionCajaDto update(Long id, SesionCajaDto sesionCajaDto) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
