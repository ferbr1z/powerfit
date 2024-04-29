package com.devs.powerfit.beans.arqueo;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.cajas.SesionCajaBean;
import com.devs.powerfit.utils.anotaciones.NotNullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@Table(name = "arqueo")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArqueoBean extends AbstractBean {

    @ManyToOne
    @JoinColumn(name = "id_sesion_caja")
    @NotNullable
    private SesionCajaBean sesionCaja;

    @Column
    @Temporal(TemporalType.DATE)
    private LocalDate fecha;

    @Column
    @Temporal(TemporalType.TIME)
    private LocalTime hora;

    @Column
    private Double montoTotal;

    @Column
    @Min(value = 0, message = "El monto de apertura no puede ser menor a cero")
    private Double montoApertura;

    @Column
    private String nombreCaja;

    @Column
    private Double totalEntradaTarjeta;

    @Column
    private Double totalEntradaEfectivo;

    @Column
    private Double totalEntradaTransferencia;

    @Column
    private Double totalSalidaTarjeta;

    @Column
    private Double totalSalidaEfectivo;

    @Column
    private Double totalSalidaTransferencia;

    @OneToMany(mappedBy = "arqueo", cascade = CascadeType.ALL)
    private List<ArqueoDetalleBean> detalles;
}
