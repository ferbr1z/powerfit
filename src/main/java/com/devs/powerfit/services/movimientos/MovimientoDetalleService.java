package com.devs.powerfit.services.movimientos;

import com.devs.powerfit.dtos.movimientos.MovimientoDetalleDto;
import com.devs.powerfit.interfaces.movimientos.IMovimientoDetalleService;
import com.devs.powerfit.interfaces.movimientos.IMovimientoService;
import com.devs.powerfit.utils.responses.PageResponse;

import java.util.List;

public class MovimientoDetalleService implements IMovimientoDetalleService {
    @Override
    public MovimientoDetalleDto create(MovimientoDetalleDto movimientoDetalleDto) {
        return null;
    }

    @Override
    public MovimientoDetalleDto getById(Long id) {
        return null;
    }

    @Override
    public PageResponse<MovimientoDetalleDto> getAll(int page) {
        return null;
    }

    @Override
    public MovimientoDetalleDto update(Long id, MovimientoDetalleDto movimientoDetalleDto) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public PageResponse<MovimientoDetalleDto> findAllByMovimiento(int page, Long movimientoId) {
        return null;
    }

    @Override
    public List<MovimientoDetalleDto> findAllByMovimiento(Long movimientoId) {
        return null;
    }

    @Override
    public PageResponse<MovimientoDetalleDto> findAllByTipoDePago(int page, Long movimientoId) {
        return null;
    }

    @Override
    public List<MovimientoDetalleDto> findAllByTipoDePago(Long movimientoId) {
        return null;
    }
}
