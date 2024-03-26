package com.devs.powerfit.beans.arqueo;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.movimientos.MovimientoBean;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "arqueo_detalle")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArqueoDetalleBean extends AbstractBean {

    @ManyToOne
    @JoinColumn(name = "arqueo_id")
    private ArqueoBean arqueo;

    @Column
    private double montoEntrada;

    @Column
    private double montoSalida;

    @ManyToOne
    @JoinColumn(name = "movimiento_id")
    private MovimientoBean movimiento;


}
