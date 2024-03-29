package com.devs.powerfit.dtos.arqueo;

import com.devs.powerfit.abstracts.AbstractDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ArqueoDto extends AbstractDto {

    private Long sesionCajaId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fecha;

    @JsonFormat(pattern = "HH:mm:ss")
    private Date hora;

    private Double montoTotal;

    private Double totalEntradaTarjeta;

    private Double totalEntradaEfectivo;

    private Double totalEntradaTransferencia;

    private Double totalSalidaTarjeta;

    private Double totalSalidaEfectivo;

    private Double totalSalidaTransferencia;

    private List<ArqueoDetalleDto> detalles;
}
