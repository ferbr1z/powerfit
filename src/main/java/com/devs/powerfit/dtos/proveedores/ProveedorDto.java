package com.devs.powerfit.dtos.proveedores;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.Data;

@Data
public class ProveedorDto extends AbstractDto {
    private String nombre;

    private String ruc;

    private String telefono;

    private String email;

    private String direccion;
}
