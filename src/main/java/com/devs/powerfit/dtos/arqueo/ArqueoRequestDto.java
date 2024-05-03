package com.devs.powerfit.dtos.arqueo;

import com.devs.powerfit.utils.anotaciones.NotNullable;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ArqueoRequestDto {


    private Long sesionCajaId;

    private Long cajaId;

    private Double montoApertura;
}
