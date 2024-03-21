package com.devs.powerfit.dtos.productos;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.Data;

@Data
public class ProductoDto extends AbstractDto{

    private String nombre;

    private String descripcion;

    private String codigo;

    private Long cantidad;

    private Double costo;

    private Double precio;

    private Double iva;

}
