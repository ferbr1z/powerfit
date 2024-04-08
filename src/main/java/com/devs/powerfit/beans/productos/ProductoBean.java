package com.devs.powerfit.beans.productos;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.utils.anotaciones.NotNullAndNotBlank;
import com.devs.powerfit.utils.anotaciones.NotNullable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "productos")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoBean extends AbstractBean {

    @Column
    @NotNullAndNotBlank
    private String nombre;

    @Column
    private String descripcion;

    @NotNullAndNotBlank
    @Size(min = 6, max = 6, message = "Debe tener exactamente 6 caracteres")
    private String codigo;

    @NotNullable
    @Column
    @Min(value = 0, message = "La cantidad debe ser mayor o igual que cero")
    private Integer cantidad;

    @NotNullable
    @Column
    @Min(value = 1, message = "El costo debe ser mayor que cero")
    private Double costo;

    @NotNullable
    @Column
    @Min(value = 1, message = "El costo debe ser mayor que cero")
    private Double precio;

    @NotNullable
    @Column
    private double iva;

    @AssertTrue(message = "El IVA debe ser 0.00, 0.05 o 0.10")
    public boolean isValidIva() {
        return iva == 0.00 || iva == 0.05 || iva == 0.10;
    }

}
