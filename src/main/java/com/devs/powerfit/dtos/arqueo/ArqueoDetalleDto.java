package com.devs.powerfit.dtos.arqueo;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.Data;

@Data
public class ArqueoDetalleDto extends AbstractDto {
    private Long arqueoId;

    private double montoEntrada;

    private double montoSalida;

    private Long movimientoId;
}
