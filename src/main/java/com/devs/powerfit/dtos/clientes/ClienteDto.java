package com.devs.powerfit.dtos.clientes;

import com.devs.powerfit.abstracts.AbstractDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClienteDto extends AbstractDto {
    @Size(max = 30, message = "El nombre no puede tener más de 30 caracteres")
    private String nombre;
    @Size(max = 20,message = "El ruc no puede tener más de 20 caracteres")
    @Pattern(regexp = "^[0-9-]*$", message = "El RUC solo puede contener números y guiones")
    private String ruc;
    @Size(max = 20,message = "La cédula no puede tener más de 20 caracteres")
    @Pattern(regexp = "^[0-9]*$", message = "La cédula solo puede contener números")
    private String cedula;
    @Size(max = 12,message = "El telefono  no puede tener más de 12 caracteres")
    @Pattern(regexp = "^[0-9]*$", message = "El teléfono solo puede contener números")
    private String telefono;

    private String email;

    @Size(max = 60, message = "La dirección no puede tener más de 60 caracteres")
    private String direccion;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaRegistro;
}
