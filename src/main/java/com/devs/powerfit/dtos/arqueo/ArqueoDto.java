package com.devs.powerfit.dtos.arqueo;

import com.devs.powerfit.abstracts.AbstractDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class ArqueoDto extends AbstractDto {

    private Long sesionCajaId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime hora;

    private Double montoTotal;

    private String nombreCaja;
    private Double montoApertura;
    private Double totalEntradaTarjeta;

    private Double totalEntradaEfectivo;

    private Double totalEntradaTransferencia;

    private Double totalSalidaTarjeta;

    private Double totalSalidaEfectivo;

    private Double totalSalidaTransferencia;

    private List<ArqueoDetalleDto> detalles;
}
