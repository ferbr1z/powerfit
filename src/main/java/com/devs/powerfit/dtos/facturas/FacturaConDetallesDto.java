package com.devs.powerfit.dtos.facturas;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.Data;

import java.util.List;

@Data
public class FacturaConDetallesDto extends AbstractDto {
    private FacturaDto factura;
    List<FacturaDetalleDto> detalles;
}
