package com.devs.powerfit.dtos.facturas;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.Data;

import java.util.List;

@Data
public class FacturaProveedorConDetallesDto extends AbstractDto {
    private FacturaProveedorDto factura;
    List<FacturaProveedorDetalleDto> detalles;

}
