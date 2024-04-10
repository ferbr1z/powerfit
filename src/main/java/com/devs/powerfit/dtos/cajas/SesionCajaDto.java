package com.devs.powerfit.dtos.cajas;

import com.devs.powerfit.abstracts.AbstractDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class SesionCajaDto extends AbstractDto {
    private Long idCaja;
    private Long idUsuario;
    private Double montoInicial;
    private Double montoFinal;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horaApertura;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horaCierre;
}
