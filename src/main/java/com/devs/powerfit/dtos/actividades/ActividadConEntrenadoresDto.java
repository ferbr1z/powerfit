package com.devs.powerfit.dtos.actividades;

import com.devs.powerfit.abstracts.AbstractDto;
import com.devs.powerfit.dtos.empleados.EmpleadoDto;
import lombok.Data;

import java.util.List;

@Data
public class ActividadConEntrenadoresDto extends AbstractDto {
    private ActividadDto actividad;
    List<EmpleadoDto> entrenadores;
}
