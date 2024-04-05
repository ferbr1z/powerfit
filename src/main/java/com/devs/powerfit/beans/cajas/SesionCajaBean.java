package com.devs.powerfit.beans.cajas;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.auth.UsuarioBean;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table(name = "sesiones_caja")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SesionCajaBean extends AbstractBean {
    @ManyToOne
    @JoinColumn(name = "id_caja")
    private CajaBean caja;
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private UsuarioBean usuario;
    @Column
    @NotNull(message = "El monto inicial no puede ser nulo")
    private Double montoInicial;
    @Column
    private Double montoFinal;
    @Column
    @NotNull(message = "La fecha no puede ser nula")
    private Date fecha;
    @Column
    @NotNull(message = "La hora de apertura no puede ser nula")
    private Date horaApertura;
    @Column
    private Date horaCierre;
}
