package com.devs.powerfit.beans.facturas;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.clientes.ClienteBean;
import com.devs.powerfit.beans.proveedores.ProveedorBean;
import com.devs.powerfit.utils.anotaciones.NotNullAndNotBlank;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "facturas_proveedores")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacturaProveedorBean extends AbstractBean {
    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private ProveedorBean proveedor;
    @Column
    @NotNullAndNotBlank
    private String timbrado;
    @Column(unique = true)
    @NotNullAndNotBlank
    private String nroFactura;
    @Column
    @NotNullAndNotBlank
    private String nombreProveedor;
    @Column
    @NotNullAndNotBlank
    private String rucProveedor;
    @Column
    @NotNull
    private Date fecha;
    @Column
    @PositiveOrZero
    private Double total;
    @Column
    @PositiveOrZero
    private Double subTotal;
    @Column
    @PositiveOrZero
    private Double saldo;
    @Column
    @PositiveOrZero
    private Double iva5;
    @Column
    @PositiveOrZero
    private Double iva10;
    @Column
    @PositiveOrZero
    private Double ivaTotal;
    @Column
    private boolean pagado;
}
