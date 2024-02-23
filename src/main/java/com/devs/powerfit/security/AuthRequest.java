package com.devs.powerfit.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthRequest {
    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;


}
