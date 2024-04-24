package com.devs.powerfit.dtos.programas;

import com.devs.powerfit.abstracts.AbstractDto;
import com.devs.powerfit.enums.ENivelPrograma;
import com.devs.powerfit.enums.ESexo;
import lombok.Data;

@Data
public class ProgramaForListDto extends AbstractDto {
    private String titulo;
    private ENivelPrograma nivel;
    private ESexo sexo;
    private String nombreActividad;
    private String nombreEmpleado;

    // Este constructor es necesario para listar los datos. ProgramaDao no funciona sin este constructor
    public ProgramaForListDto(Long id, boolean active, String titulo, ENivelPrograma nivel, ESexo sexo, String nombreActividad, String nombreEmpleado) {
        super(id, active);
        this.titulo = titulo;
        this.nivel = nivel;
        this.sexo = sexo;
        this.nombreActividad = nombreActividad;
        this.nombreEmpleado = nombreEmpleado;
    }
}
