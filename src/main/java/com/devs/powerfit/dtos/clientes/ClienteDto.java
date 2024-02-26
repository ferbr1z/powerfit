package com.devs.powerfit.dtos.clientes;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ClienteDto extends AbstractDto {
    private String nombre;
    private String ruc;
    private String cedula;
    private String telefono;
    private String email;
    private String direccion;
    private LocalDate fechaRegistro;
}
