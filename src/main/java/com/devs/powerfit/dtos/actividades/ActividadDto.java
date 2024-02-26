package com.devs.powerfit.dtos.actividades;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.Data;

@Data
public class ActividadDto extends AbstractDto {
    private String nombre;
    private String descripcion;
    private Double costoMensual;
    private Double costoSemanal;
}
