package com.devs.powerfit.dtos.tiposDePagos;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.Data;

@Data
public class TipoDePagoDto extends AbstractDto {
    private String nombre;
    private String descripcion;
}
