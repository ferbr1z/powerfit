package com.devs.powerfit.dtos.mediciones;

import com.devs.powerfit.abstracts.AbstractDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MedicionDto extends AbstractDto {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;
    private Long clienteID;
    private Double peso;
    private Double altura;
    private Double imc;
    private Double cirBrazo;
    private Double cirPiernas;
    private Double cirCintura;
    private Double cirPecho;
}
