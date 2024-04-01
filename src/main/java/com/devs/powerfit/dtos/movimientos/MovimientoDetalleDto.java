package com.devs.powerfit.dtos.movimientos;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.Data;

@Data
public class MovimientoDetalleDto extends AbstractDto {
    private Long movimientoId;
    private Long tipoDePagoId;
    private Double monto;
}
