package com.devs.powerfit.dtos.programas;

import com.devs.powerfit.dtos.actividades.ActividadDto;
import com.devs.powerfit.dtos.empleados.EmpleadoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullProgramaDto extends ProgramaDto{
    private Integer actividadId;
    private String actividad;
    private Integer empleadoId;
    private String empleado;
    private List<ProgramaItemDto> items;
}
