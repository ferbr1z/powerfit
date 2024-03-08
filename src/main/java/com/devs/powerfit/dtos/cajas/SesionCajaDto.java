package com.devs.powerfit.dtos.cajas;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.Data;

import java.util.Date;
@Data
public class SesionCajaDto extends AbstractDto {
    private Long id;
    private Long idCaja;
    private Long idUsuario;
    private Double montoInicial;
    private Double montoFinal;
    private Date fecha;
    private Date horaApertura;
    private Date horaCierre;
}
