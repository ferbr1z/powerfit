package com.devs.powerfit.beans.facturas;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.proveedores.ProveedorBean;
import com.devs.powerfit.utils.anotaciones.NotNullAndNotBlank;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    @NotNullAndNotBlank(message = "Timbrado no puede ser ni nulo ni estar en blanco")
    private String timbrado;
    @NotNullAndNotBlank(message = "Número de Factura no puede ser ni nulo ni estar en blanco")
    private String nroFactura;
    @Column
    @NotNullAndNotBlank(message = "Nombre proveedor no puede ser ni nulo ni estar en blanco")
    private String nombreProveedor;
    @Column
    @NotNullAndNotBlank(message = "Ruc Proveedor no puede ser ni nulo ni estar en blanco")
    private String rucProveedor;
    @Column
    @NotNull(message = "Fecha no puede ser nulo")
    private LocalDate fecha;
    @Column
    @Positive(message = "El valor del total debe ser positivo mayor que 0")
    @Min(value = 1,message = "El total debe ser mayor a 0")
    @NotNull(message = "El total no puede ser null")
    private Double total;
    @Column
    @Positive(message = "El valor del subtotal debe ser positivo mayor que 0")
    @Min(value = 1,message = "El subtotal debe ser mayor a 0")
    @NotNull(message = "El subtotal no puede ser null")
    private Double subTotal;
    @Column
    @PositiveOrZero(message = "El saldo debe ser positivo o 0")
    @NotNull(message = "El saldo no puede ser null")
    private Double saldo;
    @Column
    @PositiveOrZero(message = "Iva5 debe ser positivo o 0")
    @NotNull(message = "Iva5 no debe ser nulo")
    private Double iva5;
    @Column
    @PositiveOrZero(message = "Iva10 debe ser positivo o 0")
    @NotNull(message = "Iva10 no debe ser nulo")
    private Double iva10;
    @Column
    @PositiveOrZero(message = "IvaTotal debe ser positivo o 0")
    @NotNull(message = "IvaTotal no debe ser nulo")
    private Double ivaTotal;
    @Column
    @NotNull(message = "Pagado no puede ser null")
    private boolean pagado;
}
