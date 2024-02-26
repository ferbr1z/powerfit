package com.devs.powerfit.services.suscripciones;

import com.devs.powerfit.dtos.suscripciones.SuscripcionDetalleDto;
import com.devs.powerfit.interfaces.suscripciones.ISuscripcionDetalleService;
import com.devs.powerfit.utils.responses.PageResponse;

public class SuscripcionDetalleService implements ISuscripcionDetalleService {
    @Override
    public SuscripcionDetalleDto create(SuscripcionDetalleDto suscripcionDetalleDto) {
        return null;
    }

    @Override
    public SuscripcionDetalleDto getById(Long id) {
        return null;
    }

    @Override
    public PageResponse<SuscripcionDetalleDto> getAll(int page) {
        return null;
    }

    @Override
    public SuscripcionDetalleDto update(Long id, SuscripcionDetalleDto suscripcionDetalleDto) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
