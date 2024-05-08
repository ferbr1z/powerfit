package com.devs.powerfit.dtos.actividades;

import com.devs.powerfit.abstracts.AbstractDto;
import com.devs.powerfit.dtos.empleados.EmpleadoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActividadConEntrenadoresDto {
    private ActividadDto actividad;
    List<EmpleadoDto> entrenadores;
}
