package com.devs.powerfit.beans.empleados;

import com.devs.powerfit.abstracts.AbstractBean;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "empleados")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoBean extends AbstractBean {
    @Column
    private String nombre;
    @Column(unique = true)
    private String cedula;
    @Column
    private String telefono;
    @Column(unique = true)
    private String email;
    @Column
    private String direccion;
    @Column
    private Long rol_id;
}
