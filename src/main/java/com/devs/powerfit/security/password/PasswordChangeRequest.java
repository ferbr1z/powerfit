package com.devs.powerfit.security.password;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PasswordChangeRequest {
    @NotNull(message = "passActual no puede ser nulo")
    @NotBlank(message = "passActual no puede estar vacío")
    private String passActual;

    @NotNull(message = "nuevaPass no puede ser nulo")
    @NotBlank(message = "nuevaPass no puede estar vacío")
    private String nuevaPass;

    @NotNull(message = "confirmarPass no puede ser nulo")
    @NotBlank(message = "confirmarPass no puede estar vacío")
    private String confirmarPass;
}
