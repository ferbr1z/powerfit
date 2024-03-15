package com.devs.powerfit.beans.proveedores;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.utils.anotaciones.NotNullAndNotBlank;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "proveedores")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorBean extends AbstractBean {

    @NotNullAndNotBlank
    private String nombre;

    @NotNullAndNotBlank
    private String ruc;

    @NotNullAndNotBlank
    private String telefono;


    @NotNullAndNotBlank
    @Email(message = "No es válido")
    private String email;

    @NotNullAndNotBlank
    private String direccion;



}
