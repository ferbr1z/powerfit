package com.devs.powerfit.beans.actividades;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.empleados.EmpleadoBean;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "actividades")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActividadBean extends AbstractBean {
    @Column
    private String nombre;
    @Column
    private String descripcion;
    @Column
    private Double costoMensual;
    @Column
    private Double costoSemanal;

    @ManyToOne
    @JoinColumn(name = "entrenador_id")
    private EmpleadoBean entrenador;
}
