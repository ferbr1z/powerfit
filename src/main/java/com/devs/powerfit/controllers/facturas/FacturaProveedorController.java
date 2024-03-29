package com.devs.powerfit.controllers.facturas;

import com.devs.powerfit.dtos.facturas.FacturaConDetallesDto;
import com.devs.powerfit.dtos.facturas.FacturaDto;
import com.devs.powerfit.dtos.facturas.FacturaProveedorConDetallesDto;
import com.devs.powerfit.dtos.facturas.FacturaProveedorDto;
import com.devs.powerfit.interfaces.facturas.*;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/facturas-proveedores")
public class FacturaProveedorController {
    private final IFacturaProveedorDetalleService facturaDetalleService;

    private final IFacturaProveedorService facturaService;
    private IFacturaProveedorConDetallesService facturaConDetallesService;

    @Autowired
    public FacturaProveedorController(IFacturaProveedorDetalleService facturaDetalleService, IFacturaProveedorService facturaService, IFacturaProveedorConDetallesService facturaConDetallesService) {
        this.facturaDetalleService = facturaDetalleService;
        this.facturaService = facturaService;
        this.facturaConDetallesService = facturaConDetallesService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @PostMapping
    public ResponseEntity<FacturaProveedorConDetallesDto> create(@RequestBody FacturaProveedorConDetallesDto facturaDto) {
        return new ResponseEntity<>(facturaConDetallesService.create(facturaDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/{id}")
    public ResponseEntity<FacturaProveedorConDetallesDto> getById(@PathVariable Long id) {
        return new ResponseEntity<>(facturaConDetallesService.getById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/page/{page}")
    public ResponseEntity<PageResponse<FacturaProveedorConDetallesDto>> getAll(@PathVariable int page) {
        return new ResponseEntity<>(facturaConDetallesService.getAll(page), HttpStatus.OK);
    }


    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/proveedor/{nombre}/page/{page}")
    public ResponseEntity<PageResponse<FacturaProveedorDto>> searchByNombreCliente(@PathVariable String nombre, @PathVariable int page) {
        return new ResponseEntity<>(facturaService.searchByNombreProveedor(nombre, page), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/ruc/{ruc}/page/{page}")
    public ResponseEntity<PageResponse<FacturaProveedorDto>> searchByRucCliente(@PathVariable String ruc, @PathVariable int page) {
        return new ResponseEntity<>(facturaService.searchByRucProveedor(ruc, page), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/numero/{numeroFactura}")
    public ResponseEntity<FacturaProveedorDto> searchByNumeroFactura(@PathVariable String numeroFactura) {
        return new ResponseEntity<>(facturaService.searchByNumeroFactura(numeroFactura), HttpStatus.OK);
    }
}

