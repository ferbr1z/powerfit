package com.devs.powerfit.beans.productos;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.utils.anotaciones.NotNullAndNotBlank;
import com.devs.powerfit.utils.anotaciones.NotNullable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "productos")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoBean extends AbstractBean {

    @Column
    private String nombre;

    @Column
    private String descripcion;

    @NotNullAndNotBlank
    @Size(min = 6, max = 6, message = "Debe tener exactamente 6 caracteres")
    private String codigo;

    @NotNullable
    @Column
    private Long cantidad;

    @Column
    private Double costo;

    @Column
    private Double precio;

}
