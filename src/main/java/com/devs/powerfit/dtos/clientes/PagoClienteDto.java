package com.devs.powerfit.dtos.clientes;

import com.devs.powerfit.abstracts.AbstractDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import lombok.Data;

import java.util.Date;

@Data
public class PagoClienteDto extends AbstractDto {
    private Double monto;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fecha;
    private String nroFactura;
}
