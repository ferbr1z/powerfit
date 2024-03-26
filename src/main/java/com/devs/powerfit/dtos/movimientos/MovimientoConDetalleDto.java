package com.devs.powerfit.dtos.movimientos;

import com.devs.powerfit.abstracts.AbstractDto;

import java.util.List;

public class MovimientoConDetalleDto extends AbstractDto {
    private MovimientoDto movimiento;
    List<MovimientoConDetalleDto> detalles;
}
