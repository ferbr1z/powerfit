package com.devs.powerfit.dtos.programas;

import com.devs.powerfit.abstracts.AbstractDto;
import com.devs.powerfit.enums.ENivelPrograma;
import com.devs.powerfit.enums.ESexo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramaDto extends AbstractDto {
    protected Long id;
    protected String titulo;
    protected ENivelPrograma nivel;
    protected ESexo sexo;
}
