package com.devs.powerfit.beans.actividades;

import com.devs.powerfit.abstracts.AbstractBean;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
