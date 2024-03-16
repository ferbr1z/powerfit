package com.devs.powerfit.controllers.actividades;

import com.devs.powerfit.dtos.actividades.ActividadConClientesDto;
import com.devs.powerfit.dtos.actividades.ActividadDto;
import com.devs.powerfit.dtos.suscripciones.SuscripcionConClienteDto;
import com.devs.powerfit.interfaces.actividades.IActividadService;
import com.devs.powerfit.services.actividades.ActividadConClientesService;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/actividades")
public class ActividadController {

    private final IActividadService actividadService;
    private final ActividadConClientesService actividadConClientesService;
    @Autowired
    public ActividadController(IActividadService actividadService, ActividadConClientesService actividadConClientesService) {
        this.actividadService = actividadService;
        this.actividadConClientesService = actividadConClientesService;
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @PostMapping
    public ResponseEntity<ActividadDto> create(@RequestBody ActividadDto actividadDto) {
        return new ResponseEntity<>(actividadService.create(actividadDto), HttpStatus.CREATED);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/{id}")
    public ResponseEntity<ActividadDto> getById(@PathVariable Long id) {
        return new ResponseEntity<>(actividadService.getById(id), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/page/{page}")
    public ResponseEntity<PageResponse<ActividadDto>> getAll(@PathVariable int page) {
        return new ResponseEntity<>(actividadService.getAll(page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/count/clientes/page/{page}")
    public ResponseEntity<PageResponse<ActividadConClientesDto>> getAllWithClientes(@PathVariable int page) {
        return new ResponseEntity<>(actividadConClientesService.getAllActividadesConClientes(page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/{id}/suscripciones/page/{page}")
    public ResponseEntity<PageResponse<SuscripcionConClienteDto>> getSuscripcionesWithClientesPorActividad(@PathVariable Long id, @PathVariable int page) {
        return new ResponseEntity<>(actividadConClientesService.getSuscripcionesConClientesPorActividad(id,page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @PutMapping("/{id}")
    public ResponseEntity<ActividadDto> update(@PathVariable Long id, @RequestBody ActividadDto actividadDto) {
        return new ResponseEntity<>(actividadService.update(id, actividadDto), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return new ResponseEntity<>(actividadService.delete(id), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/search/{nombre}/page/{page}")
    public ResponseEntity<PageResponse<ActividadDto>> searchByName(@PathVariable int page, @PathVariable String nombre) {
        return new ResponseEntity<>(actividadService.searchByNombre(nombre,page), HttpStatus.OK);
    }
}

