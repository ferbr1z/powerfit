package com.devs.powerfit.beans.clientes;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.utils.anotaciones.NotNullAndNotBlank;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@Table(name = "clientes")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteBean extends AbstractBean {
    @Column
    @NotNullAndNotBlank(message = "El nombre del cliente no puede ser nulo")
    private String nombre;
    @NotNullAndNotBlank(message = "El ruc del cliente no puede ser nulo")
    private String ruc;
    @Column(unique = true)
    @NotNullAndNotBlank(message = "La cédula del cliente no puede ser nulo")
    private String cedula;
    @Column
    private String telefono;
    @Column(unique = true)
    private String email;
    @Column
    private String direccion;
    @Column
    private LocalDate fechaRegistro;
}