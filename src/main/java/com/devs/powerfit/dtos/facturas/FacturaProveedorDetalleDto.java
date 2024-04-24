package com.devs.powerfit.dtos.facturas;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.Data;

@Data
public class FacturaProveedorDetalleDto extends AbstractDto {
    private Long facturaId;
    private Long productoId;
    private Double precioUnitario;
    private String productoNombre;
    private Integer cantidad;
    private Double subtotal;
    private Double iva;
    private Double ivaTotal;
}
