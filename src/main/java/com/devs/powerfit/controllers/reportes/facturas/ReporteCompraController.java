package com.devs.powerfit.controllers.reportes.facturas;

import com.devs.powerfit.dtos.facturas.filtros.ReporteComprasFilterDto;
import com.devs.powerfit.dtos.facturas.reportes.ReporteComprasDto;
import com.devs.powerfit.services.facturas.FacturaProveedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/compras")
public class ReporteCompraController {
    private final FacturaProveedorService service;
    @Autowired
    public ReporteCompraController(FacturaProveedorService service) {
        this.service = service;
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @PostMapping("/page/{page}")
    ResponseEntity<ReporteComprasDto> filtrarCompras(@RequestBody @Valid ReporteComprasFilterDto filtro, @PathVariable int page){
        return new ResponseEntity<>(service.filtrarFacturas(filtro,page), HttpStatus.OK);
    }
}
