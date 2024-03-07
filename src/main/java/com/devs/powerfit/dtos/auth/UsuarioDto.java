package com.devs.powerfit.dtos.auth;

import lombok.Data;

@Data
public class UsuarioDto {
    private String nombre;
    private String email;
    private String password;
    private Long rol_id;
}
