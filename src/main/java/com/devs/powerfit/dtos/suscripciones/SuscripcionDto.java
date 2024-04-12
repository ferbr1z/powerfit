package com.devs.powerfit.dtos.suscripciones;

import com.devs.powerfit.abstracts.AbstractDto;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class SuscripcionDto extends AbstractDto {
    private Long clienteId;
    private Long actividadId;
    private String actividadNombre;
    private Double costoMensual;
    private Double costoSemanal;
    private String descripcion;
    private String estado;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaInicio;
    @JsonFormat(pattern = "yyyy-MM-dd")
<<<<<<< HEAD
    private Date fechaFin;
    private Double monto;
=======
    private LocalDate fechaFin;
>>>>>>> dev
    private String modalidad;
}
