package com.devs.powerfit.dtos.empleados;

import com.devs.powerfit.abstracts.AbstractDto;

import lombok.Data;

@Data
public class EmpleadoDto extends AbstractDto {

    private String nombre;
    private String ruc;
    private String cedula;
    private String telefono;
    private String email;
    private String direccion;
    private Long rol_id;
}
