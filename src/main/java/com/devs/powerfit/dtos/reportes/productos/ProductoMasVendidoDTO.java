package com.devs.powerfit.dtos.reportes.productos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoMasVendidoDTO {
    private String nombreProducto;
    private int cantidadVendida;
}
