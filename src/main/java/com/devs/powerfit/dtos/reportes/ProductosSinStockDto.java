package com.devs.powerfit.dtos.reportes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductosSinStockDto {
    Long productoId;
    String nombre;
}
