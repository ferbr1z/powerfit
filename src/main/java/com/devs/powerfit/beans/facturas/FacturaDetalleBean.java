package com.devs.powerfit.beans.facturas;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.productos.ProductoBean;
import com.devs.powerfit.beans.suscripciones.SuscripcionBean;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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
    private Double precioUnitario;
    @Column
    @Min(value = 1,message = "No se puede realizar una factura con cantidad 0")
    private Integer cantidad;
    @Column
    @PositiveOrZero
    private Double subTotal;
    @Column
    @PositiveOrZero
    private Double iva;
    @Column
    @PositiveOrZero
    private double ivaTotal;
}
