package com.devs.powerfit.interfaces.movimientos;

import com.devs.powerfit.dtos.movimientos.MovimientoDetalleDto;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.utils.responses.PageResponse;

import java.util.List;

public interface IMovimientoDetalleService extends IService<MovimientoDetalleDto> {
    PageResponse<MovimientoDetalleDto> findAllByMovimiento(int page, Long movimientoId);
    List<MovimientoDetalleDto> findAllByMovimiento(Long movimientoId);
    PageResponse<MovimientoDetalleDto> findAllByTipoDePago(int page, Long movimientoId);
    List<MovimientoDetalleDto> findAllByTipoDePago(Long movimientoId);
}
