package com.devs.powerfit.beans;

import com.devs.powerfit.abstracts.AbstractBean;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "clientes")
public class ClienteBean extends AbstractBean {

    @Column
    private String nombre;
    private String ruc;
    @Column(unique = true)
    private String cedula;
    @Column
    private String telefono;
    @Column
    private String email;
    @Column
    private String direccion;
    @Column
    private LocalDate fechaRegistro;
}