package com.devs.powerfit.dtos.facturas;

import com.devs.powerfit.enums.EEstado;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FacturaFilterDTO {
    private Boolean estadoPago;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;
    private Double valorDeuda;
    private String proveedor;
    private String ruc;
}
