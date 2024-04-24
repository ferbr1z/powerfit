package com.devs.powerfit.beans.actividades;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.empleados.EmpleadoBean;
import com.devs.powerfit.utils.anotaciones.NotNullAndNotBlank;
import com.devs.powerfit.utils.anotaciones.NotNullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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
    @NotNullAndNotBlank
    private String nombre;
    @Column
    private String descripcion;
    @Column
    @NotNullable
    @Min(value = 0, message = "El costo mensual de la actividad no puede ser negativo.")
    private Double costoMensual;
    @Column
    @NotNullable
    @Min(value = 0, message = "El costo semanal de la actividad no puede ser negativo.")
    private Double costoSemanal;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "actividad_entrenador",
            joinColumns = @JoinColumn(name = "actividad_id"),
            inverseJoinColumns = @JoinColumn(name = "entrenador_id"))
    private List<EmpleadoBean> entrenadores;
}
