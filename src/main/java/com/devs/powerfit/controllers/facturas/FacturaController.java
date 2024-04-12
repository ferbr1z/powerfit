package com.devs.powerfit.controllers.facturas;

import com.devs.powerfit.dtos.cajas.SesionCajaDto;
import com.devs.powerfit.dtos.facturas.FacturaConDetallesDto;
import com.devs.powerfit.dtos.facturas.FacturaDetalleDto;
import com.devs.powerfit.dtos.facturas.FacturaDto;
import com.devs.powerfit.interfaces.facturas.IFacturaConDetallesService;
import com.devs.powerfit.interfaces.facturas.IFacturaDetalleService;
import com.devs.powerfit.interfaces.facturas.IFacturaService;
import com.devs.powerfit.services.facturas.FacturaConDetallesService;
import com.devs.powerfit.services.facturas.FacturaDetalleService;
import com.devs.powerfit.services.facturas.FacturaService;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/facturas")
public class FacturaController {
    private final FacturaDetalleService facturaDetalleService;

    private final FacturaService facturaService;
    private FacturaConDetallesService facturaConDetallesService;

    @Autowired
    public FacturaController(FacturaDetalleService facturaDetalleService, FacturaService facturaService, FacturaConDetallesService facturaConDetallesService) {
        this.facturaDetalleService = facturaDetalleService;
        this.facturaService = facturaService;
        this.facturaConDetallesService = facturaConDetallesService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @PostMapping
    public ResponseEntity<FacturaConDetallesDto> create(@RequestBody FacturaConDetallesDto facturaDto) {
        return new ResponseEntity<>(facturaConDetallesService.create(facturaDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/{id}")
    public ResponseEntity<FacturaConDetallesDto> getById(@PathVariable Long id) {
        return new ResponseEntity<>(facturaConDetallesService.getById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/page/{page}")
    public ResponseEntity<PageResponse<FacturaConDetallesDto>> getAll(@PathVariable int page) {
        return new ResponseEntity<>(facturaConDetallesService.getAll(page), HttpStatus.OK);
    }
    @GetMapping("/{fechaInicio}/{fechaFin}/page/{page}")
    ResponseEntity<PageResponse<FacturaConDetallesDto>> getAllBetween(@PathVariable int page, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin){
        return new ResponseEntity<>(facturaConDetallesService.searchByFecha(page,fechaInicio, fechaFin), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/cliente/{nombre}/page/{page}")
    public ResponseEntity<PageResponse<FacturaConDetallesDto>> searchByNombreCliente(@PathVariable String nombre, @PathVariable int page) {
        return new ResponseEntity<>(facturaConDetallesService.getAllByNombreCliente(nombre, page), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/ruc/{ruc}/page/{page}")
    public ResponseEntity<PageResponse<FacturaConDetallesDto>> searchByRucCliente(@PathVariable String ruc, @PathVariable int page) {
        return new ResponseEntity<>(facturaConDetallesService.getAllByRucCliente(ruc, page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/estado/pagado/page/{page}")
    public ResponseEntity<PageResponse<FacturaConDetallesDto>> searchByPagado( @PathVariable int page) {
        return new ResponseEntity<>(facturaConDetallesService.searchByPagado( page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/estado/pendiente/page/{page}")
    public ResponseEntity<PageResponse<FacturaConDetallesDto>> searchByPendiente( @PathVariable int page) {
        return new ResponseEntity<>(facturaConDetallesService.searchByPendiente( page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/numero/{numeroFactura}")
    public ResponseEntity<FacturaConDetallesDto> searchByNumeroFactura(@PathVariable String numeroFactura) {
        return new ResponseEntity<>(facturaConDetallesService.getByNumeroFactura(numeroFactura), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/cabecera/numero/{numeroFactura}")
    public ResponseEntity<FacturaDto> searchCabeceraByNumeroFactura(@PathVariable String numeroFactura) {
        return new ResponseEntity<>(facturaService.searchByNumeroFactura(numeroFactura), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/cabecera/estado/pagado/page/{page}")
    public ResponseEntity<PageResponse<FacturaDto>> searchCabeceraByPagado( @PathVariable int page) {
        return new ResponseEntity<>(facturaService.searchByPagado( page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/cabecera/estado/pendiente/page/{page}")
    public ResponseEntity<PageResponse<FacturaDto>> searchCabeceraByPendiente( @PathVariable int page) {
        return new ResponseEntity<>(facturaService.searchByPendiente( page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/cabecera/cliente/{nombre}/page/{page}")
    public ResponseEntity<PageResponse<FacturaDto>> searchCabeceraByNombreCliente(@PathVariable String nombre, @PathVariable int page) {
        return new ResponseEntity<>(facturaService.searchByNombreCliente(nombre, page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/cabecera/ruc/{ruc}/page/{page}")
    public ResponseEntity<PageResponse<FacturaDto>> searchCabeceraByRucCliente(@PathVariable String ruc, @PathVariable int page) {
        return new ResponseEntity<>(facturaService.searchByRucCliente(ruc, page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/cabecera/estado/pagado/cliente/{nombre}/page/{page}")
    public ResponseEntity<PageResponse<FacturaDto>> searchCabeceraByNombreAndPagado(@PathVariable String nombre, @PathVariable int page) {
        return new ResponseEntity<>(facturaService.searchByNombreClienteAndPagado(nombre, page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/cabecera/estado/pendiente/cliente/{nombre}/page/{page}")
    public ResponseEntity<PageResponse<FacturaDto>> searchCabeceraByNombreAndPendiente(@PathVariable String nombre, @PathVariable int page) {
        return new ResponseEntity<>(facturaService.searchByNombreClienteAndPendiente(nombre, page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/cabecera/page/{page}")
    public ResponseEntity<PageResponse<FacturaDto>> getAllCabecera(@PathVariable int page) {
        return new ResponseEntity<>(facturaService.getAll(page), HttpStatus.OK);
    }

}

