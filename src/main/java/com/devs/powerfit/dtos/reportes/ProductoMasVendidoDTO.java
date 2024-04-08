package com.devs.powerfit.dtos.reportes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoMasVendidoDTO {
//    private Long id;
    private String nombreProducto;
    private int cantidadVendida;
}
