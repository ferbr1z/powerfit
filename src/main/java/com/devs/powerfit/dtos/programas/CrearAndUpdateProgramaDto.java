package com.devs.powerfit.dtos.programas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearAndUpdateProgramaDto extends ProgramaDto {
    private Long actividad;
    private Long empleado;
}
