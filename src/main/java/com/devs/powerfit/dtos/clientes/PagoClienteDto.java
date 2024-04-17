package com.devs.powerfit.dtos.clientes;

import com.devs.powerfit.abstracts.AbstractDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PagoClienteDto extends AbstractDto {
    private Double monto;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;
    private String nroFactura;
}
