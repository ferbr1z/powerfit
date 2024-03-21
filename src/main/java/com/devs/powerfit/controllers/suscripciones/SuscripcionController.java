package com.devs.powerfit.controllers.suscripciones;

import com.devs.powerfit.dtos.suscripciones.SuscripcionDto;
import com.devs.powerfit.services.suscripciones.SuscripcionService;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suscripciones")
public class SuscripcionController {

    private final SuscripcionService suscripcionService;

    @Autowired
    public SuscripcionController(SuscripcionService suscripcionService) {
        this.suscripcionService = suscripcionService;
    }
    //Suscripciones
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    public ResponseEntity<List <SuscripcionDto>> create(@RequestBody List<SuscripcionDto> suscripcionesDto) {
        return new ResponseEntity<>(suscripcionService.createList(suscripcionesDto), HttpStatus.CREATED);
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
    @GetMapping("/cliente/{id}/page/{page}")
    public ResponseEntity<PageResponse<SuscripcionDto>> SearchByClientId(@PathVariable Long id,@PathVariable int page) {
        PageResponse<SuscripcionDto> suscripcionesPage = suscripcionService.getAllByClientId(id,page);
        return new ResponseEntity<>(suscripcionesPage, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/cliente/{id}/pendientes/page/{page}")
    public ResponseEntity<PageResponse<SuscripcionDto>> getAllPendientesByClientId(@PathVariable Long id, @PathVariable int page) {
        return new ResponseEntity<>(suscripcionService.getAllPendientesByClientId(id, page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/cliente/{id}/pagados/page/{page}")
    public ResponseEntity<PageResponse<SuscripcionDto>> getAllPagadosByClientId(@PathVariable Long id, @PathVariable int page) {
        return new ResponseEntity<>(suscripcionService.getAllPagadosByClientId(id, page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/pagados/page/{page}")
    public ResponseEntity<PageResponse<SuscripcionDto>> getAllPagados( @PathVariable int page) {
        return new ResponseEntity<>(suscripcionService.getAllPagados( page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/pendientes/page/{page}")
    public ResponseEntity<PageResponse<SuscripcionDto>> getAllPendientes( @PathVariable int page) {
        return new ResponseEntity<>(suscripcionService.getAllPendientes( page), HttpStatus.OK);
    }










}
