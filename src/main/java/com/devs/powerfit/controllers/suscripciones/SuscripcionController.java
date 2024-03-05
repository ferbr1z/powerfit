package com.devs.powerfit.controllers.suscripciones;

import com.devs.powerfit.dtos.suscripciones.SuscripcionConDetallesDto;
import com.devs.powerfit.dtos.suscripciones.SuscripcionDetalleDto;
import com.devs.powerfit.dtos.suscripciones.SuscripcionDto;
import com.devs.powerfit.interfaces.suscripciones.ISuscripcionConDetalleService;
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
    private final ISuscripcionConDetalleService suscripcionConDetalleService;

    @Autowired
    public SuscripcionController(ISuscripcionService suscripcionService, ISuscripcionDetalleService suscripcionDetalleService, ISuscripcionConDetalleService suscripcionConDetalleService) {
        this.suscripcionService = suscripcionService;
        this.suscripcionDetalleService = suscripcionDetalleService;
        this.suscripcionConDetalleService = suscripcionConDetalleService;
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

    @PostMapping("/detalles")
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    public ResponseEntity<SuscripcionDetalleDto> create(@RequestBody SuscripcionDetalleDto suscripcionDetalleDto) {
        return new ResponseEntity<>(suscripcionDetalleService.create(suscripcionDetalleDto), HttpStatus.CREATED);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/detalles/{id}")
    public ResponseEntity<SuscripcionDetalleDto> getDetalleById(@PathVariable Long id) {
        SuscripcionDetalleDto suscripcionDetalleDto = suscripcionDetalleService.getById(id);
        return new ResponseEntity<>(suscripcionDetalleDto, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/detalles/page/{page}")
    public ResponseEntity<PageResponse<SuscripcionDetalleDto>> getAllDetalles(@PathVariable int page) {
        PageResponse<SuscripcionDetalleDto> suscripcionesPage = suscripcionDetalleService.getAll(page);
        return new ResponseEntity<>(suscripcionesPage, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @PutMapping("/detalles/{id}")
    public ResponseEntity<SuscripcionDetalleDto> update(@PathVariable Long id, @RequestBody SuscripcionDetalleDto suscripcionDetalleDto) {
        SuscripcionDetalleDto updatedSuscripcion = suscripcionDetalleService.update(id, suscripcionDetalleDto);
        return new ResponseEntity<>(updatedSuscripcion, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @DeleteMapping("/detalles/{id}")
    public ResponseEntity<Boolean> deleteDetalle(@PathVariable Long id) {
        boolean deleted = suscripcionDetalleService.delete(id);
        return new ResponseEntity<>(deleted, HttpStatus.OK);
    }
    // Implementación de endpoints para suscripciones con detalles

    @PostMapping("/con-detalles")
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    public ResponseEntity<SuscripcionConDetallesDto> createWithDetails(@RequestBody SuscripcionConDetallesDto suscripcionConDetallesDto) {
        SuscripcionConDetallesDto createdSuscripcion = suscripcionConDetalleService.create(suscripcionConDetallesDto);
        return new ResponseEntity<>(createdSuscripcion, HttpStatus.CREATED);
    }

    @GetMapping("/con-detalles/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    public ResponseEntity<SuscripcionConDetallesDto> getByIdWithDetails(@PathVariable Long id) {
        SuscripcionConDetallesDto suscripcionConDetallesDto = suscripcionConDetalleService.getById(id);
        return new ResponseEntity<>(suscripcionConDetallesDto, HttpStatus.OK);
    }

    @PutMapping("/con-detalles/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    public ResponseEntity<SuscripcionConDetallesDto> updateWithDetails(@PathVariable Long id, @RequestBody SuscripcionConDetallesDto suscripcionConDetallesDto) {
        SuscripcionConDetallesDto updatedSuscripcion = suscripcionConDetalleService.update(id, suscripcionConDetallesDto);
        return new ResponseEntity<>(updatedSuscripcion, HttpStatus.OK);
    }

    @DeleteMapping("/con-detalles/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    public ResponseEntity<Boolean> deleteWithDetails(@PathVariable Long id) {
        boolean deleted = suscripcionConDetalleService.delete(id);
        return new ResponseEntity<>(deleted, HttpStatus.OK);
    }

    @GetMapping("/con-detalles/page/{page}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    public ResponseEntity<PageResponse<SuscripcionConDetallesDto>> getAllWithDetails(@PathVariable int page) {
        PageResponse<SuscripcionConDetallesDto> suscripcionesConDetallesPage = suscripcionConDetalleService.getAll(page);
        return new ResponseEntity<>(suscripcionesConDetallesPage, HttpStatus.OK);
    }
}
