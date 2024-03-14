package com.devs.powerfit.dtos.clientes;

import com.devs.powerfit.abstracts.AbstractDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ClienteDto extends AbstractDto {
    private String nombre;
    private String ruc;
    private String cedula;
    private String telefono;
    private String email;
    private String direccion;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaRegistro;
}
