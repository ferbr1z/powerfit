package com.devs.powerfit.dtos.programas;

import com.devs.powerfit.dtos.actividades.ActividadDto;
import com.devs.powerfit.dtos.empleados.EmpleadoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullProgramaDto extends ProgramaDto{
    private ActividadDto actividad;
    private EmpleadoDto empleado;
}
