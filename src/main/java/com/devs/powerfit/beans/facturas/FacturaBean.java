package com.devs.powerfit.beans.facturas;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.clientes.ClienteBean;
import com.devs.powerfit.beans.suscripciones.SuscripcionDetalleBean;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "facturas")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacturaBean extends AbstractBean {
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private ClienteBean cliente;
    @Column
    private String timbrado;
    @Column
    private String nroFactura;
    @Column
    private String nombreCliente;
    @Column
    private String rucCliente;
    @Column
    private Date fecha;
    @Column
    private Double total;
    @Column
    private Double subTotal;
    @Column
    private Double saldo;
    @Column
    private Double iva5;
    @Column
    private Double iva10;
    @Column
    private Double ivaTotal;
    @Column
    private boolean pagado;
}
