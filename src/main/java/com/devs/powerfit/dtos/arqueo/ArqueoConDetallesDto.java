package com.devs.powerfit.dtos.arqueo;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.Data;

import java.util.List;

@Data
public class ArqueoConDetallesDto extends AbstractDto {
    private ArqueoDto arqueo;
    List<ArqueoDetalleDto> detalles;

}
