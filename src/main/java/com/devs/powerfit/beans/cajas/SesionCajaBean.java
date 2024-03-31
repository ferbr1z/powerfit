package com.devs.powerfit.beans.cajas;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.auth.UsuarioBean;
import com.devs.powerfit.beans.empleados.EmpleadoBean;
import jakarta.persistence.*;
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
    @JoinColumn(name = "id_empleado")
    private EmpleadoBean empleado;
    @Column
    private Double montoInicial;
    @Column
    private Double montoFinal;
    @Column
    private Date fecha;
    @Column
    private Date horaApertura;
    @Column
    private Date horaCierre;
}
