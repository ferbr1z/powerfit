package com.devs.powerfit.dtos.cajas;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.Data;

@Data
public class CajaDto extends AbstractDto {
    private String nombre;
    private Double monto;
    private Long numeroCaja;
    private Long numeroFactura;
}
