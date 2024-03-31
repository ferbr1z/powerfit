package com.devs.powerfit.dtos.actividades;

import com.devs.powerfit.abstracts.AbstractDto;
import com.devs.powerfit.dtos.empleados.EmpleadoDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
public class ActividadDto extends AbstractDto {
    private String nombre;
    private String descripcion;
    private Double costoMensual;
    private Double costoSemanal;
    private Long entrenador;
}
