package com.devs.powerfit.dtos.facturas.filtros;

import com.devs.powerfit.utils.anotaciones.FacturaNumeroFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReporteComprasFilterDto {
    @Size(max = 30, message = "El nombre del proveedor no puede exceder los 30 caracteres")
    private String nombreProveedor;
    @Pattern(regexp = "^[0-9-]+$", message = "El RUC del proveedor solo puede contener números y guiones")
    @Size(max = 20, message = "El RUC del proveedor no puede exceder los 20 caracteres")
    private String rucProveedor;
    private String numeroFactura;
    private Boolean pagado;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaInicio;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaFin;
    @AssertTrue(message = "La fecha de fin debe ser posterior o igual a la fecha de inicio")
    public boolean isFechaFinValid() {
        return fechaFin == null || fechaInicio == null || !fechaFin.isBefore(fechaInicio);
    }
}
