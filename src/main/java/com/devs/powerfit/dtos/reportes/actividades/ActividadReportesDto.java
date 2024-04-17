package com.devs.powerfit.dtos.reportes.actividades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActividadReportesDto {
    private String actividad;
    private Integer cantidad;
}
