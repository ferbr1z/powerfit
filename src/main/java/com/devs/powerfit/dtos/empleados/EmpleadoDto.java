package com.devs.powerfit.dtos.empleados;

import com.devs.powerfit.abstracts.AbstractDto;

import com.devs.powerfit.dtos.actividades.ActividadDto;
import lombok.Data;

import java.util.Set;

@Data
public class EmpleadoDto extends AbstractDto {

    private String nombre;
    private String cedula;
    private String telefono;
    private String email;
    private String direccion;
    private Long rol;
}
