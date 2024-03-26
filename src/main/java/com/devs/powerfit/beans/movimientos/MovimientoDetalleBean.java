package com.devs.powerfit.beans.movimientos;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.tipoDePagos.TipoDePagoBean;
import jakarta.persistence.*;
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
    private MovimientoBean movimiento;
    @ManyToOne
    @JoinColumn(name = "tipo_de_pago_id")
    private TipoDePagoBean tipoDePago;
    @Column
    @PositiveOrZero
    private Double monto;
}
