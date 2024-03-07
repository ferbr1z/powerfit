package com.devs.powerfit.beans.suscripciones;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.actividades.ActividadBean;
import com.devs.powerfit.enums.EEstado;
import com.devs.powerfit.enums.EModalidad;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.time.LocalDate;
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuscripcionDetalleBean extends AbstractBean {
    @ManyToOne
    @JoinColumn(name = "id_suscripcion")
    private SuscripcionBean suscripcion;
    @ManyToOne
    @JoinColumn(name = "id_actividad")
    private ActividadBean actividad;
    @Enumerated(EnumType.STRING)
    private EEstado estado;
    @Enumerated(EnumType.STRING)
    private EModalidad modalidad;
    @Column
    private Date fechaInicio;
    @Column
    Date fechaFin;

}
