package com.devs.powerfit.controllers.facturas;

import com.devs.powerfit.beans.facturas.FacturaProveedorBean;
import com.devs.powerfit.dtos.facturas.*;
import com.devs.powerfit.interfaces.facturas.*;
import com.devs.powerfit.services.facturas.FacturaProveedorConDetallesService;
import com.devs.powerfit.services.facturas.FacturaProveedorDetalleService;
import com.devs.powerfit.services.facturas.FacturaProveedorService;
import com.devs.powerfit.utils.responses.PageResponse;
import com.devs.powerfit.utils.specifications.FacturaSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/facturas-proveedores")
public class FacturaProveedorController {
    private final FacturaProveedorDetalleService facturaDetalleService;

    private final FacturaProveedorService facturaService;
    private FacturaProveedorConDetallesService facturaConDetallesService;

    @Autowired
    public FacturaProveedorController(FacturaProveedorDetalleService facturaDetalleService, FacturaProveedorService facturaService, FacturaProveedorConDetallesService facturaConDetallesService) {
        this.facturaDetalleService = facturaDetalleService;
        this.facturaService = facturaService;
        this.facturaConDetallesService = facturaConDetallesService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @PostMapping
    public ResponseEntity<FacturaProveedorConDetallesDto> create(@RequestBody FacturaProveedorConDetallesDto facturaDto) {
        return new ResponseEntity<>(facturaConDetallesService.create(facturaDto), HttpStatus.CREATED);
    }
    @GetMapping("/{fechaInicio}/{fechaFin}/page/{page}")
    ResponseEntity<PageResponse<FacturaProveedorConDetallesDto>> getAllBetween(@PathVariable int page, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin){
        return new ResponseEntity<>(facturaConDetallesService.searchByFecha(page,fechaInicio, fechaFin), HttpStatus.OK);
    }


    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/{id}")
    public ResponseEntity<FacturaProveedorConDetallesDto> getById(@PathVariable Long id) {
        return new ResponseEntity<>(facturaConDetallesService.getById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/page/{page}")
    public ResponseEntity<PageResponse<FacturaProveedorConDetallesDto>> getAll(@PathVariable int page) {
        return new ResponseEntity<>(facturaConDetallesService.getAll(page), HttpStatus.OK);
    }


    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/proveedor/{nombre}/page/{page}")
    public ResponseEntity<PageResponse<FacturaProveedorDto>> searchByNombreCliente(@PathVariable String nombre, @PathVariable int page) {
        return new ResponseEntity<>(facturaService.searchByNombreProveedor(nombre, page), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/ruc/{ruc}/page/{page}")
    public ResponseEntity<PageResponse<FacturaProveedorDto>> searchByRucCliente(@PathVariable String ruc, @PathVariable int page) {
        return new ResponseEntity<>(facturaService.searchByRucProveedor(ruc, page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/estado/pendiente/page/{page}")
    public ResponseEntity<PageResponse<FacturaProveedorDto>> searchByPendiente( @PathVariable int page) {
        return new ResponseEntity<>(facturaService.searchByPendiente( page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/estado/pagado/page/{page}")
    public ResponseEntity<PageResponse<FacturaProveedorDto>> searchByPagado( @PathVariable int page) {
        return new ResponseEntity<>(facturaService.searchByPagado( page), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/numero/{numeroFactura}")
    public ResponseEntity<FacturaProveedorDto> searchByNumeroFactura(@PathVariable String numeroFactura) {
        return new ResponseEntity<>(facturaService.searchByNumeroFactura(numeroFactura), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @PostMapping("/filter/page/{page}")
    public ResponseEntity<PageResponse<FacturaProveedorDto>> filtrarFacturas(@RequestBody FacturaFilterDTO filtro, @PathVariable int page) {
        Specification<FacturaProveedorBean> spec = FacturaSpecification.fromFiltro(filtro);
        return new ResponseEntity<>(facturaService.filtrarFacturas(spec, page), HttpStatus.OK);
    }
}

