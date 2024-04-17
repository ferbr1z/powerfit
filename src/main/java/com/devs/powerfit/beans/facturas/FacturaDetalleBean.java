package com.devs.powerfit.beans.facturas;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.productos.ProductoBean;
import com.devs.powerfit.beans.suscripciones.SuscripcionBean;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "factura_detalles")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacturaDetalleBean extends AbstractBean {
    @ManyToOne
    @JoinColumn(name = "factura_id")
    private FacturaBean factura;
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private ProductoBean producto;
    @ManyToOne
    @JoinColumn(name = "suscripcion_id")
    private SuscripcionBean suscripcion;
    @Column
    @PositiveOrZero(message = "El precio unitario no puede ser negativo")
    private Double precioUnitario;
    @Column
    @Positive(message = "La cantidad debe ser positiva")
    @Min(value = 1,message = "No se puede realizar una factura con cantidad 0")
    private Integer cantidad;
    @Column
    @PositiveOrZero(message = "El subtotal no puede ser negativo ni 0")
    private Double subTotal;
    @Column
    @PositiveOrZero(message = "El iva debe ser positivo o 0")
    private Double iva;
    @Column
    @PositiveOrZero(message = "El ivaTotal debe ser positivo o 0")
    private double ivaTotal;
}
