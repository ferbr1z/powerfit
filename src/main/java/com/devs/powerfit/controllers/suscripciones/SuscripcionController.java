package com.devs.powerfit.controllers.suscripciones;

import com.devs.powerfit.dtos.suscripciones.SuscripcionDetalleDto;
import com.devs.powerfit.dtos.suscripciones.SuscripcionDto;
import com.devs.powerfit.interfaces.suscripciones.ISuscripcionDetalleService;
import com.devs.powerfit.utils.responses.PageResponse;
import com.devs.powerfit.interfaces.suscripciones.ISuscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/suscripciones")
public class SuscripcionController {
    private final ISuscripcionService suscripcionService;
    private final ISuscripcionDetalleService suscripcionDetalleService;

    @Autowired
    public SuscripcionController(ISuscripcionService suscripcionService, ISuscripcionDetalleService suscripcionDetalleService) {
        this.suscripcionService = suscripcionService;
        this.suscripcionDetalleService = suscripcionDetalleService;
    }
    //Suscripciones
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    public ResponseEntity<SuscripcionDto> create(@RequestBody SuscripcionDto suscripcionDto) {
        return new ResponseEntity<>(suscripcionService.create(suscripcionDto), HttpStatus.CREATED);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/{id}")
    public ResponseEntity<SuscripcionDto> getById(@PathVariable Long id) {
        SuscripcionDto suscripcionDto = suscripcionService.getById(id);
        return new ResponseEntity<>(suscripcionDto, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/page/{page}")
    public ResponseEntity<PageResponse<SuscripcionDto>> getAll(@PathVariable int page) {
        PageResponse<SuscripcionDto> suscripcionesPage = suscripcionService.getAll(page);
        return new ResponseEntity<>(suscripcionesPage, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @PutMapping("/{id}")
    public ResponseEntity<SuscripcionDto> update(@PathVariable Long id, @RequestBody SuscripcionDto suscripcionDto) {
        SuscripcionDto updatedSuscripcion = suscripcionService.update(id, suscripcionDto);
        return new ResponseEntity<>(updatedSuscripcion, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        boolean deleted = suscripcionService.delete(id);
        return new ResponseEntity<>(deleted, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/search/{nombre}/page/{page}")
    public ResponseEntity<PageResponse<SuscripcionDto>> searchByName(@PathVariable int page, @PathVariable String nombre) {
        return new ResponseEntity<>(suscripcionService.searchByNombreCliente(nombre,page), HttpStatus.OK);
    }
    //Detalles
    @PostMapping("/detalle")
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    public ResponseEntity<SuscripcionDetalleDto> create(@RequestBody SuscripcionDetalleDto suscripcionDetalleDto) {
        return new ResponseEntity<>(suscripcionDetalleService.create(suscripcionDetalleDto), HttpStatus.CREATED);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/detalle/{id}")
    public ResponseEntity<SuscripcionDetalleDto> getDetalleById(@PathVariable Long id) {
        SuscripcionDetalleDto suscripcionDetalleDto = suscripcionDetalleService.getById(id);
        return new ResponseEntity<>(suscripcionDetalleDto, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/detalle/page/{page}")
    public ResponseEntity<PageResponse<SuscripcionDetalleDto>> getAllDetalles(@PathVariable int page) {
        PageResponse<SuscripcionDetalleDto> suscripcionesPage = suscripcionDetalleService.getAll(page);
        return new ResponseEntity<>(suscripcionesPage, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @PutMapping("/detalle/{id}")
    public ResponseEntity<SuscripcionDetalleDto> update(@PathVariable Long id, @RequestBody SuscripcionDetalleDto suscripcionDetalleDto) {
        SuscripcionDetalleDto updatedSuscripcion = suscripcionDetalleService.update(id, suscripcionDetalleDto);
        return new ResponseEntity<>(updatedSuscripcion, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @DeleteMapping("/detalle/{id}")
    public ResponseEntity<Boolean> deleteDetalle(@PathVariable Long id) {
        boolean deleted = suscripcionDetalleService.delete(id);
        return new ResponseEntity<>(deleted, HttpStatus.OK);
    }
}
