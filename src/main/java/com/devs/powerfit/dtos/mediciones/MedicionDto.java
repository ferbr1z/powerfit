package com.devs.powerfit.dtos.mediciones;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.Data;

import java.util.Date;

@Data
public class MedicionDto extends AbstractDto {
    private Date fecha;
    private Long clienteID;
    private Double peso;
    private Double altura;
    private Double imc;
    private Double cirBrazo;
    private Double cirPiernas;
    private Double cirCintura;
    private Double cirPecho;
}
