package com.devs.powerfit.beans.auth;

import com.devs.powerfit.abstracts.AbstractBean;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "password_recovery_tokens")
public class PasswordRecoveryTokenBean extends AbstractBean {

    @NotNull(message = "El token no puede ser nulo")
    private String token;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario")
    @NotNull(message = "El usuario no puede ser nulo")
    private UsuarioBean usuario;

    private LocalDate fechaExpiracion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaUso;
    private Boolean usado;
}
