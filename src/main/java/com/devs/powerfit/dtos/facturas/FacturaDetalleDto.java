package com.devs.powerfit.dtos.facturas;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.Data;

@Data
public class FacturaDetalleDto extends AbstractDto {
    private Long facturaId;
    private Long productoId;
    private Long subscripcionId;
    private Double precioUnitario;
    private Integer cantidad;
    private Double subtotal;
    private Double ivaGenerado;
}
