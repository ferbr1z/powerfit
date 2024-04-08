package com.devs.powerfit.dtos.productos;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDto extends AbstractDto{

    private String nombre;

    private String descripcion;

    private String codigo;

    private Integer cantidad;

    private Double costo;

    private Double precio;

    private Double iva;


}
