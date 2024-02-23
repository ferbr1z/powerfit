package com.devs.powerfit.security;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private String email;
    private String accessToken;
    private Long rol_id;
    private String nombre;

    public AuthResponse() {
        throw new IllegalArgumentException("User email and access token are required");
    }
}
