package com.devs.powerfit.controllers.facturas;

import com.devs.powerfit.dtos.facturas.FacturaConDetallesDto;
import com.devs.powerfit.dtos.facturas.FacturaDetalleDto;
import com.devs.powerfit.dtos.facturas.FacturaDto;
import com.devs.powerfit.interfaces.facturas.IFacturaConDetallesService;
import com.devs.powerfit.interfaces.facturas.IFacturaDetalleService;
import com.devs.powerfit.interfaces.facturas.IFacturaService;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/facturas")
public class FacturaController {
    private final IFacturaDetalleService facturaDetalleService;

    private final IFacturaService facturaService;
    private IFacturaConDetallesService facturaConDetallesService;

    @Autowired
    public FacturaController(IFacturaDetalleService facturaDetalleService, IFacturaService facturaService, IFacturaConDetallesService facturaConDetallesService) {
        this.facturaDetalleService = facturaDetalleService;
        this.facturaService = facturaService;
        this.facturaConDetallesService = facturaConDetallesService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @PostMapping
    public ResponseEntity<FacturaConDetallesDto> create(@RequestBody FacturaConDetallesDto facturaDto) {
        return new ResponseEntity<>(facturaConDetallesService.create(facturaDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/{id}")
    public ResponseEntity<FacturaConDetallesDto> getById(@PathVariable Long id) {
        return new ResponseEntity<>(facturaConDetallesService.getById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/page/{page}")
    public ResponseEntity<PageResponse<FacturaConDetallesDto>> getAll(@PathVariable int page) {
        return new ResponseEntity<>(facturaConDetallesService.getAll(page), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/cliente/{nombre}/page/{page}")
    public ResponseEntity<PageResponse<FacturaDto>> searchByNombreCliente(@PathVariable String nombre, @PathVariable int page) {
        return new ResponseEntity<>(facturaService.searchByNombreCliente(nombre, page), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/ruc/{ruc}/page/{page}")
    public ResponseEntity<PageResponse<FacturaDto>> searchByRucCliente(@PathVariable String ruc, @PathVariable int page) {
        return new ResponseEntity<>(facturaService.searchByRucCliente(ruc, page), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/numero/{numeroFactura}")
    public ResponseEntity<FacturaDto> searchByNumeroFactura(@PathVariable String numeroFactura) {
        return new ResponseEntity<>(facturaService.searchByNumeroFactura(numeroFactura), HttpStatus.OK);
    }
}

