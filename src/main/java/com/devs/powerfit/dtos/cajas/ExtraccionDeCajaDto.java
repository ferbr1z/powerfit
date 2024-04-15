package com.devs.powerfit.dtos.cajas;

import com.devs.powerfit.abstracts.AbstractDto;
import com.devs.powerfit.utils.anotaciones.NotNullAndNotBlank;
import com.devs.powerfit.utils.anotaciones.NotNullable;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtraccionDeCajaDto extends AbstractDto {
    private Long idCaja;

    private Long idUsuario;

    private String nombreCaja;

    private String nombreUsuario;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime hora;

    private Double monto;

}
