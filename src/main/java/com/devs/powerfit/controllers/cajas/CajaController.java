package com.devs.powerfit.controllers.cajas;

import com.devs.powerfit.dtos.actividades.ActividadDto;
import com.devs.powerfit.dtos.cajas.CajaDto;
import com.devs.powerfit.dtos.cajas.SesionCajaDto;
import com.devs.powerfit.interfaces.cajas.ICajaService;
import com.devs.powerfit.interfaces.cajas.ISesionCajaService;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cajas")
public class CajaController {

    private final ICajaService cajaService;
    private final ISesionCajaService sesionCajaService;

    @Autowired
    public CajaController(ICajaService cajaService, ISesionCajaService sesionCajaService) {
        this.cajaService = cajaService;
        this.sesionCajaService = sesionCajaService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @PostMapping
    public ResponseEntity<CajaDto> create(@RequestBody CajaDto cajaDto) {
        return new ResponseEntity<>(cajaService.create(cajaDto), HttpStatus.CREATED);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/search/{nombre}/page/{page}")
    public ResponseEntity<PageResponse<CajaDto>> searchByName(@PathVariable int page, @PathVariable String nombre) {
        return new ResponseEntity<>(cajaService.searchByNombre(nombre,page), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/{id}")
    public ResponseEntity<CajaDto> getById(@PathVariable Long id) {
        return new ResponseEntity<>(cajaService.getById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/page/{page}")
    public ResponseEntity<PageResponse<CajaDto>> getAll(@PathVariable int page) {
        return new ResponseEntity<>(cajaService.getAll(page), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @PutMapping("/{id}")
    public ResponseEntity<CajaDto> update(@PathVariable Long id, @RequestBody CajaDto cajaDto) {
        return new ResponseEntity<>(cajaService.update(id, cajaDto), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return new ResponseEntity<>(cajaService.delete(id), HttpStatus.OK);
    }
    //Sesiones caja
    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @PostMapping("/sesiones-caja")
    public ResponseEntity<SesionCajaDto> createSesion(@RequestBody SesionCajaDto cajaDto) {
        return new ResponseEntity<>(sesionCajaService.create(cajaDto), HttpStatus.CREATED);
    }
    // Obtener una sesión de caja por su ID
    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/sesiones-caja/{id}")
    public ResponseEntity<SesionCajaDto> getSesionCajaById(@PathVariable Long id) {
        return new ResponseEntity<>(sesionCajaService.getById(id), HttpStatus.OK);
    }

    // Obtener todas las sesiones de caja paginadas
    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/sesiones-caja/page/{page}")
    public ResponseEntity<PageResponse<SesionCajaDto>> getAllSesionesCaja(@PathVariable int page) {
        return new ResponseEntity<>(sesionCajaService.getAll(page), HttpStatus.OK);
    }

    // Actualizar una sesión de caja
    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @PutMapping("/sesiones-caja/{id}")
    public ResponseEntity<SesionCajaDto> updateSesionCaja(@PathVariable Long id, @RequestBody SesionCajaDto sesionCajaDto) {
        return new ResponseEntity<>(sesionCajaService.update(id, sesionCajaDto), HttpStatus.OK);
    }


}
