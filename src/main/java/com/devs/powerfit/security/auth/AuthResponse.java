package com.devs.powerfit.security.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private String email;
    private String accessToken;
    private Long rol;
    private String nombre;
    private Long usuarioId;

}
