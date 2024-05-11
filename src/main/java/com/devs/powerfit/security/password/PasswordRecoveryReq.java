package com.devs.powerfit.security.password;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PasswordRecoveryReq {
    @NotNull(message = "La contraseña es requerida")
    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;
}
