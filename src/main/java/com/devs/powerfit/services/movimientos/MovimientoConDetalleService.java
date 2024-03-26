package com.devs.powerfit.services.movimientos;

import com.devs.powerfit.dtos.movimientos.MovimientoConDetalleDto;
import com.devs.powerfit.interfaces.movimientos.IMovimientoConDetalleService;
import com.devs.powerfit.utils.responses.PageResponse;

public class MovimientoConDetalleService implements IMovimientoConDetalleService {
    @Override
    public MovimientoConDetalleDto create(MovimientoConDetalleDto movimientoConDetalleDto) {
        return null;
    }

    @Override
    public MovimientoConDetalleDto getById(Long id) {
        return null;
    }

    @Override
    public PageResponse<MovimientoConDetalleDto> getAll(int page) {
        return null;
    }

    @Override
    public MovimientoConDetalleDto update(Long id, MovimientoConDetalleDto movimientoConDetalleDto) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
