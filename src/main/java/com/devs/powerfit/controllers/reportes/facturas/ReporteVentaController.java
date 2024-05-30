package com.devs.powerfit.controllers.reportes.facturas;

import com.devs.powerfit.dtos.facturas.filtros.ReporteVentasFilterDto;
import com.devs.powerfit.dtos.facturas.reportes.ReporteVentasDto;
import com.devs.powerfit.dtos.reportes.actividades.ActividadReportesDto;
import com.devs.powerfit.services.facturas.FacturaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ventas")
public class ReporteVentaController {
    private final FacturaService facturaService;
    @Autowired
    public ReporteVentaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @PostMapping("/page/{page}")
    ResponseEntity<ReporteVentasDto> filtrarVentas(@RequestBody @Valid ReporteVentasFilterDto filtro,@PathVariable int page){
        return new ResponseEntity<>(facturaService.filtrarFacturas(filtro,page), HttpStatus.OK);
    }
}
