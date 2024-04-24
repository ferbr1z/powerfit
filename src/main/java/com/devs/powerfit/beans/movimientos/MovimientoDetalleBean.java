package com.devs.powerfit.beans.movimientos;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.tipoDePagos.TipoDePagoBean;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "movimientos_detalle")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoDetalleBean extends AbstractBean {
    @ManyToOne
    @JoinColumn(name = "movimiento_id")
    @NotNull(message = "Movimiento no puede ser null")
    private MovimientoBean movimiento;
    @ManyToOne
    @JoinColumn(name = "tipo_de_pago_id")
    @NotNull(message = "Tipo de pago no puede ser null")
    private TipoDePagoBean tipoDePago;
    @Column
    @PositiveOrZero(message = "El monto debe ser positivo")
    @Min(value = 1,message = "El monto debe ser positivo")
    @NotNull(message = "El monto no debe ser nulo")
    private Double monto;
}
