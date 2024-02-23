package com.devs.powerfit.beans;

import com.devs.powerfit.abstracts.AbstractBean;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "actividades")
public class ActividadBean extends AbstractBean {
    @Column
    private String nombre;
    @Column
    private String descripcion;
    @Column
    private Double costoMensual;
    @Column
    private Double costoSemanal;

}
