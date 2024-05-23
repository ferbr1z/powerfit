package com.devs.powerfit.dtos.facturas.reportes;

import com.devs.powerfit.dtos.facturas.FacturaDto;
import com.devs.powerfit.dtos.facturas.FacturaProveedorDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReporteVentasDto {
    private double total;
    private double totalPagado;
    private double totalPendiente;
    private List<FacturaDto> facturas;
    private int totalPages;
    private long totalElements;
    private int currentPage;
}
