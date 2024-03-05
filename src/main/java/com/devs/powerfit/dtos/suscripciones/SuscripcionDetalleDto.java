package com.devs.powerfit.dtos.suscripciones;

import com.devs.powerfit.abstracts.AbstractDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SuscripcionDetalleDto extends AbstractDto {
    private Long subscripcionId;
    private Long actividadId;
    private String estado;
    private Date fechaInicio;
    private Date fechaFin;
    private String modalidad;
}
