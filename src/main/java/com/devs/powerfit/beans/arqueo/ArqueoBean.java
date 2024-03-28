package com.devs.powerfit.beans.arqueo;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.cajas.SesionCajaBean;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
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
    private SesionCajaBean sesionCaja;

    @Column
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @Column
    @Temporal(TemporalType.TIME)
    private Date hora;

    @Column
    private Double montoTotal;

    @OneToMany(mappedBy = "arqueo", cascade = CascadeType.ALL)
    private List<ArqueoDetalleBean> detalles;
}
