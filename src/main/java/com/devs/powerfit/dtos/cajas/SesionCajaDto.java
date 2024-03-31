package com.devs.powerfit.dtos.cajas;

import com.devs.powerfit.abstracts.AbstractDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
@Data
public class SesionCajaDto extends AbstractDto {
    private Long idCaja;
    private Long idEmpleado;
    private Double montoInicial;
    private Double montoFinal;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fecha;
    @JsonFormat(pattern = "HH:mm:ss")
    private Date horaApertura;
    @JsonFormat(pattern = "HH:mm:ss")
    private Date horaCierre;
}
