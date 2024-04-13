package com.devs.powerfit.controllers.movimientos;

import com.devs.powerfit.dtos.movimientos.MovimientoConDetalleDto;
import com.devs.powerfit.dtos.productos.ProductoDto;
import com.devs.powerfit.services.movimientos.MovimientoConDetalleService;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {
    private final MovimientoConDetalleService service;
    @Autowired
    public MovimientoController(MovimientoConDetalleService service) {
        this.service = service;
    }
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    public ResponseEntity<MovimientoConDetalleDto> create(@RequestBody MovimientoConDetalleDto movimientoConDetalleDto) {
        return new ResponseEntity<>(service.create(movimientoConDetalleDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/{id}")
    ResponseEntity<MovimientoConDetalleDto> getById(@PathVariable Long id){
        return new ResponseEntity<>(service.getById(id), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/page/{page}")
    ResponseEntity<PageResponse<MovimientoConDetalleDto>> getAll(@PathVariable int page){
        return new ResponseEntity<>(service.getAll(page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/sesion/{id}/page/{page}")
    ResponseEntity<PageResponse<MovimientoConDetalleDto>> getAll(@PathVariable Long id,@PathVariable int page){
        return new ResponseEntity<>(service.getAllBySesionId(page,id), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/search/nombre/{name}/page/{page}")
    ResponseEntity<PageResponse<MovimientoConDetalleDto>> getAll(@PathVariable String name,@PathVariable int page){
        return new ResponseEntity<>(service.searchByComprobanteNombre(page,name), HttpStatus.OK);
    }
    //No funcional
    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/search/facturas/page/{page}")
    public ResponseEntity<PageResponse<MovimientoConDetalleDto>> searchFacturasMovimientos(
            @PathVariable int page,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Boolean entrada,
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin,
            @RequestParam(required = false) String nombreCaja) {

        PageResponse<MovimientoConDetalleDto> response = service.searchMovimientoByNombreAndFacturaAndEntradaAndFechaBetweenAndNombreCaja(
                page, nombre, entrada, fechaInicio, fechaFin, nombreCaja);

        return ResponseEntity.ok(response);
    }

    //No funcional
    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/search/facturas-proveedores/page/{page}")
    public ResponseEntity<PageResponse<MovimientoConDetalleDto>> searchFacturasProveedoresMovimientos(
            @PathVariable int page,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Boolean entrada,
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin,
            @RequestParam(required = false) String nombreCaja) {

        PageResponse<MovimientoConDetalleDto> response = service.searchMovimientoByNombreAndFacturaProveedorAndEntradaAndFechaBetweenAndNombreCaja(
                page, nombre, entrada, fechaInicio, fechaFin, nombreCaja);

        return ResponseEntity.ok(response);
    }



}
