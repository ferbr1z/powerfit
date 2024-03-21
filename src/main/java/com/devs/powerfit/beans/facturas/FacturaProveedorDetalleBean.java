package com.devs.powerfit.beans.facturas;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.abstracts.AbstractDto;
import com.devs.powerfit.beans.productos.ProductoBean;
import com.devs.powerfit.beans.suscripciones.SuscripcionBean;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private FacturaProveedorBean factura;
    @ManyToOne
    @JoinColumn(name = "producto_id")
    @NotNull
    private ProductoBean producto;
    @Column
    @PositiveOrZero
    private Double precioUnitario;
    @Column
    @PositiveOrZero
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
