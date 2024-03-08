package com.devs.powerfit.beans.productos;

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
@Table(name = "productos")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoBean extends AbstractBean {

    @Column
    private String nombre;

    @Column
    private String descripcion;

    @Column(unique = true)
    private String codigo;

    @Column
    private Long cantidad;

    @Column
    private Double costo;

    @Column
    private Double precio;

}
