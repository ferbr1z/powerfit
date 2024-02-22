package com.devs.powerfit.dtos;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.Data;

@Data
public class ClienteDto extends AbstractDto {
    private String nombre;
    private String ruc;
    private String cedula;
}
