package com.devs.powerfit.dtos.suscripciones;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.Data;

@Data
public class SuscripcionesEstadisticasDto extends AbstractDto {
    private Long cantidadClientesMorosos;
    private Long cantidadClientesEnRegla;
}
