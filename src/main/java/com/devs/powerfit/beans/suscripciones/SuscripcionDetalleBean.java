package com.devs.powerfit.beans.suscripciones;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.actividades.ActividadBean;
import com.devs.powerfit.beans.suscripciones.SuscripcionBean;
import com.devs.powerfit.enums.EEstado;
import com.devs.powerfit.enums.EModalidad;
import jakarta.persistence.*;

import java.time.LocalDate;

public class SuscripcionDetalleBean extends AbstractBean {
    @ManyToOne
    @JoinColumn(name = "id_suscripcion")
    private SuscripcionBean suscripcion;
    @OneToOne
    @JoinColumn(name = "id_actividad")
    private ActividadBean actividad;
    @Enumerated(EnumType.STRING)
    private EEstado estado;
    @Enumerated(EnumType.STRING)
    private EModalidad modalidad;
    @Column
    private LocalDate fechaInicio;
    @Column LocalDate fechaFin;

}
