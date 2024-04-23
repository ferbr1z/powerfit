package com.devs.powerfit.dtos.programas;

import com.devs.powerfit.abstracts.AbstractDto;
import com.devs.powerfit.dtos.actividades.ActividadDto;
import com.devs.powerfit.dtos.empleados.EmpleadoDto;
import com.devs.powerfit.enums.ENivelPrograma;
import com.devs.powerfit.enums.ESexo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramaDto extends AbstractDto {
    private Long id;
    private String titulo;
    private ENivelPrograma nivel;
    private ESexo sexo;
    private Long actividad;
    private Long empleado;
}
