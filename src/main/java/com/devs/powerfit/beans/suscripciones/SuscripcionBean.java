package com.devs.powerfit.beans.suscripciones;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.actividades.ActividadBean;
import com.devs.powerfit.beans.clientes.ClienteBean;
import com.devs.powerfit.enums.EEstado;
import com.devs.powerfit.enums.EModalidad;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuscripcionBean extends AbstractBean {
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private ClienteBean cliente;
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
