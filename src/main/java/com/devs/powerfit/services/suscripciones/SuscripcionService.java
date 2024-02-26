package com.devs.powerfit.services.suscripciones;

import com.devs.powerfit.dtos.suscripciones.SuscripcionDto;
import com.devs.powerfit.interfaces.suscripciones.ISuscripcionService;
import com.devs.powerfit.utils.responses.PageResponse;

public class SuscripcionService implements ISuscripcionService {
    @Override
    public SuscripcionDto create(SuscripcionDto suscripcionDto) {
        return null;
    }

    @Override
    public SuscripcionDto getById(Long id) {
        return null;
    }

    @Override
    public PageResponse<SuscripcionDto> getAll(int page) {
        return null;
    }

    @Override
    public SuscripcionDto update(Long id, SuscripcionDto suscripcionDto) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
