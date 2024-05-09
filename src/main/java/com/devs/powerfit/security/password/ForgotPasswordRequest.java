package com.devs.powerfit.security.password;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
    @NotNull(message = "El email no puede ser nulo")
    @NotBlank(message = "El email no puede estar vacío")
    private String email;
}
