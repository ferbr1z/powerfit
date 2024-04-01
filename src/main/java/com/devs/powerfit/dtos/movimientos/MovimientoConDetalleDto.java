package com.devs.powerfit.dtos.movimientos;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.Data;

import java.util.List;
@Data
public class MovimientoConDetalleDto extends AbstractDto {
    private MovimientoDto movimiento;
    List<MovimientoDetalleDto> detalles;
}
