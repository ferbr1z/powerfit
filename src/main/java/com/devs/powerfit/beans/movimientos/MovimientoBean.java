package com.devs.powerfit.beans.movimientos;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.cajas.ExtraccionDeCajaBean;
import com.devs.powerfit.beans.cajas.SesionCajaBean;
import com.devs.powerfit.beans.facturas.FacturaBean;
import com.devs.powerfit.beans.facturas.FacturaProveedorBean;
import com.devs.powerfit.utils.anotaciones.NotNullAndNotBlank;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

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
    @JoinColumn(name = "extraccion_id")
    private ExtraccionDeCajaBean extraccion;
    @ManyToOne
    @NotNull(message = "Sesion no puede ser null")
    @JoinColumn(name = "sesion_id")
    private SesionCajaBean sesion;
    @Column
    @NotNull(message = "Fecha no puede ser nula")
    private LocalDate fecha;
    @Column
    @NotNull(message = "Hora no puede ser nula")
    private LocalTime hora;
    @Column
    @Positive(message = "El total debe ser positivo")
    @NotNull(message = "El total no puede ser nulo")
    @Min(value = 1,message = "Total debe ser mayor a 0")
    private Double total;
    private boolean entrada;
    @NotNullAndNotBlank(message = "El nombre de caja no puede ser nulo ni en blanco")
    private String nombreCaja;
    @NotNullAndNotBlank(message = "El nombre de empleado no puede ser nulo ni en blanco")
    private String nombreEmpleado;
    private String comprobanteNombre;
    //como se agregaron extracciones, no se puede hacer not null porque no siempre se va a tener un comprobante
    //@NotNullAndNotBlank(message = "El numero de comprobante no puede ser nulo ni en blanco")
    private String comprobanteNumero;
}
