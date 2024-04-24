package com.devs.powerfit.beans.empleados;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.utils.anotaciones.NotNullAndNotBlank;
import com.devs.powerfit.utils.anotaciones.NotNullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
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
    @NotNullAndNotBlank
    private String nombre;

    @NotNullAndNotBlank
    @Column(unique = true)
    private String cedula;

    @Column
    @NotNullAndNotBlank
    private String telefono;

    @Column(unique = true)
    @NotNullAndNotBlank
    @Email(message = "Email invalido")
    private String email;

    @NotNullAndNotBlank
    @Column
    private String direccion;

    @NotNullable
    @Column
    private Long rol;

    @AssertTrue(message = "El rol debe ser 1, 3 o 4")
    private boolean isValidRolId(){
        return rol == 1 || rol == 3 || rol == 4;
    }
}
