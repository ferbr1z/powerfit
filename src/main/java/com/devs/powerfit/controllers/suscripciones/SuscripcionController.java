package com.devs.powerfit.controllers.suscripciones;

import com.devs.powerfit.dtos.clientes.ClienteDto;
import com.devs.powerfit.dtos.suscripciones.SuscripcionDto;
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

    @Autowired
    public SuscripcionController(ISuscripcionService suscripcionService) {
        this.suscripcionService = suscripcionService;
    }

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
}
