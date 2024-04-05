package com.devs.powerfit.beans.facturas;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.abstracts.AbstractDto;
import com.devs.powerfit.beans.productos.ProductoBean;
import com.devs.powerfit.beans.suscripciones.SuscripcionBean;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "facturas_proveedores_detalles")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FacturaProveedorDetalleBean extends AbstractBean {
    @ManyToOne
    @JoinColumn(name = "factura_id")
    @NotNull(message = "La factura proveedor no puede ser null")
    private FacturaProveedorBean factura;
    @ManyToOne
    @JoinColumn(name = "producto_id")
    @NotNull(message = "El producto no puede ser null")
    private ProductoBean producto;
    @Column
    @Positive(message = "El precio unitario debe ser positivo")
    @Min(value = 1,message = "El precio unitario debe ser superior a 0")
    @NotNull(message = "Precio Unitario no debe ser nulo")
    private Double precioUnitario;
    @Column
    @Positive(message = "La cantidad debe ser positiva")
    @Min(value = 1,message = "La cantidad minima es 1")
    private Integer cantidad;
    @Column
    @PositiveOrZero(message = "El subtotal debe ser superior a 0")
    @Min(value = 1,message = "Subtotal no puede ser 0")
    private Double subTotal;
    @Column
    @PositiveOrZero(message = "Iva debe ser positivo o 0")
    @NotNull(message = "Iva no debe ser nulo")
    private Double iva;
    @Column
    @PositiveOrZero(message = "IvaTotal debe ser positivo o 0")
    @NotNull(message = "IvaTotal no debe ser nulo")
    private double ivaTotal;
}
