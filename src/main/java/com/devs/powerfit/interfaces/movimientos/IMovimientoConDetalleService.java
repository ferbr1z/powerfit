package com.devs.powerfit.interfaces.movimientos;

import com.devs.powerfit.dtos.movimientos.MovimientoConDetalleDto;
import com.devs.powerfit.dtos.movimientos.MovimientoDetalleDto;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.services.movimientos.MovimientoConDetalleService;

import java.util.List;

public interface IMovimientoConDetalleService extends IService<MovimientoConDetalleDto> {
    List<MovimientoConDetalleDto> findAllBySesionId(Long sesionId);
}
