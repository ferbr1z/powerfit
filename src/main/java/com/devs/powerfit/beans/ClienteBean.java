package com.devs.powerfit.beans;

import com.devs.powerfit.abstracts.AbstractBean;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "clientes")
public class ClienteBean extends AbstractBean {

    @Column
    private String nombre;
    @Column(unique = true)
    private String ruc;
    @Column(unique = true)
    private String cedula;
}