package com.devs.powerfit.dtos.facturas.filtros;

import com.devs.powerfit.utils.anotaciones.FacturaNumeroFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReporteVentasFilterDto {
    @Size(max = 30, message = "El nombre del cliente no puede exceder los 30 caracteres")
    private String nombreCliente;
    @Size(max = 30, message = "El nombre del empleado no puede exceder los 30 caracteres")
    private String nombreEmpleado;
    @Pattern(regexp = "^[0-9-]+$", message = "El RUC del cliente solo puede contener números y guiones")
    @Size(max = 20, message = "El RUC del cliente no puede exceder los 20 caracteres")
    private String rucCliente;
    @FacturaNumeroFormat(message = "El número de factura debe tener el formato 001-001-00000001")
    @Size(max=16,message = "El número de factura no puede exceder los 16 caracteres")
    private String numeroFactura;
    private Boolean pagado;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaInicio;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaFin;
    @Min(value = 1, message = "El número de página debe ser por lo menos 1")
    private int page;
    @AssertTrue(message = "La fecha de fin debe ser posterior o igual a la fecha de inicio")
    public boolean isFechaFinValid() {
        return fechaFin == null || fechaInicio == null || !fechaFin.isBefore(fechaInicio);
    }
}
