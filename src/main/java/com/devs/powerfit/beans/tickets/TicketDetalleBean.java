package com.devs.powerfit.beans.tickets;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.productos.ProductoBean;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "ticket_detalles")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDetalleBean extends AbstractBean {
    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private TicketBean ticket;
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private ProductoBean producto;
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
