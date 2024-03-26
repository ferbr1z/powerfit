package com.devs.powerfit.beans.movimientos;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.cajas.SesionCajaBean;
import com.devs.powerfit.beans.facturas.FacturaBean;
import com.devs.powerfit.beans.facturas.FacturaProveedorBean;
import com.devs.powerfit.utils.anotaciones.NotNullAndNotBlank;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table(name = "movimientos")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoBean extends AbstractBean {
    @ManyToOne
    @JoinColumn(name = "factura_id")
    private FacturaBean factura;
    @ManyToOne
    @JoinColumn(name = "factura_proveedor_id")
    private FacturaProveedorBean facturaProveedor;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "sesion_id")
    private SesionCajaBean sesion;
    @Column
    @NotNullAndNotBlank
    private Date fecha;
    @Column
    @NotNullAndNotBlank
    private Date hora;
    @Column
    @Positive
    private Double total;
    private boolean entrada;
}
