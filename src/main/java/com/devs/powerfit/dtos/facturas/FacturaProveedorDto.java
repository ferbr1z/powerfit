package com.devs.powerfit.dtos.facturas;

import com.devs.powerfit.abstracts.AbstractDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FacturaProveedorDto extends AbstractDto {
    private Long proveedorId;
    private String timbrado;
    private String nroFactura;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;
    private String nombreProveedor;
    private String rucProveedor;
    private Double total;
    private Double subTotal;
    private Double saldo;
    private Double iva5;
    private Double iva10;
    private Double ivaTotal;
    private boolean pagado;
}
