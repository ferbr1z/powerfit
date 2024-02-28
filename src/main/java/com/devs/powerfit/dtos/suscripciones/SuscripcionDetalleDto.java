package com.devs.powerfit.dtos.suscripciones;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SuscripcionDetalleDto extends AbstractDto {
    private Long subscripcionId;
    private Long actividadId;
    private String estado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String modalidad;
}
