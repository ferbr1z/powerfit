package com.devs.powerfit.dtos.suscripciones;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class SuscripcionGananciasDto extends AbstractDto {
    Double gananciasPotenciales;
    Double gananciaActual;
    Double perdidasMorosos;
}
