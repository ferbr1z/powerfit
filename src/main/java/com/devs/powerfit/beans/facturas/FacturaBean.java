package com.devs.powerfit.beans.facturas;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.cajas.SesionCajaBean;
import com.devs.powerfit.beans.clientes.ClienteBean;
import com.devs.powerfit.utils.anotaciones.NotNullAndNotBlank;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    @ManyToOne
    @JoinColumn(name = "sesion_id")
    private SesionCajaBean sesion;
    @Column
    @NotNullAndNotBlank(message = "El timbrado no puede ser null ni en blanco")
    private String timbrado;
    private String direccion;
    @NotNullAndNotBlank(message = "El nombre de caja no puede ser nulo ni en blanco")
    private String nombreCaja;
    @NotNullAndNotBlank(message = "El nombre de empleado no puede ser nulo ni en blanco")
    private String nombreEmpleado;
    @Column
    @NotNullAndNotBlank(message = "El numero de factura no puede ser null ni en blanco")
    private String nroFactura;
    @Column
    private String nombreCliente;
    @Column
    private String rucCliente;
    @Column
    private LocalDate fecha;
    @Column
    @PositiveOrZero(message = "El total debe ser positivo o 0")
    private Double total;
    @Column
    @PositiveOrZero(message = "El subtotal debe ser positivo o 0")
    private Double subTotal;
    @Column
    @PositiveOrZero(message = "El saldo debe ser positivo o 0")
    private Double saldo;
    @Column
    @PositiveOrZero(message = "El iva5 debe ser positivo o 0")
    private Double iva5;
    @Column
    @PositiveOrZero(message = "El iva10 debe ser positivo o 0")
    private Double iva10;
    @Column
    @PositiveOrZero(message = "El ivatotal debe ser positivo o 0")
    private Double ivaTotal;
    @Column
    private boolean pagado;
}
