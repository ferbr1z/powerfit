package com.devs.powerfit.beans.cajas;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.empleados.EmpleadoBean;
import com.devs.powerfit.utils.anotaciones.NotNullAndNotBlank;
import com.devs.powerfit.utils.anotaciones.NotNullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "extracciones_caja")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtraccionDeCajaBean extends AbstractBean {
    @ManyToOne
    @JoinColumn(name = "id_caja")
    private CajaBean caja;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private EmpleadoBean usuario;

    @Column
    @NotNullAndNotBlank
    private String nombreCaja;

    @Column
    @NotNullAndNotBlank
    private String nombreUsuario;

    @Column
    @Temporal(TemporalType.DATE)
    @NotNullable
    private LocalDate fecha;

    @Column
    @Temporal(TemporalType.TIME)
    @NotNullable
    private LocalTime hora;

    @Column
    @NotNullable
    private Double monto;

    @ManyToOne
    @NotNull(message = "Sesion no puede ser null")
    @JoinColumn(name = "sesion_id")
    private SesionCajaBean sesion;


}
