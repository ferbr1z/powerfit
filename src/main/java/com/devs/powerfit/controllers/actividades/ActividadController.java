package com.devs.powerfit.controllers.actividades;

import com.devs.powerfit.dtos.actividades.ActividadConClientesDto;
import com.devs.powerfit.dtos.actividades.ActividadConEntrenadoresDto;
import com.devs.powerfit.dtos.actividades.ActividadDto;
import com.devs.powerfit.dtos.suscripciones.SuscripcionConClienteDto;
import com.devs.powerfit.interfaces.actividades.IActividadService;
import com.devs.powerfit.services.actividades.ActividadConClientesService;
import com.devs.powerfit.services.actividades.ActividadConEntrenadoresService;
import com.devs.powerfit.utils.responses.PageResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/actividades")
public class ActividadController {

    private final IActividadService actividadService;
    private final ActividadConClientesService actividadConClientesService;
    private final ActividadConEntrenadoresService actividadConEntrenadoresService;
    @Autowired
    public ActividadController(ActividadConEntrenadoresService actividadConEntrenadoresService, IActividadService actividadService, ActividadConClientesService actividadConClientesService) {
        this.actividadService = actividadService;
        this.actividadConClientesService = actividadConClientesService;
        this.actividadConEntrenadoresService = actividadConEntrenadoresService;
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    @PostMapping
    public ResponseEntity<ActividadDto> create(@Valid @RequestBody ActividadDto actividadDto) {
        return new ResponseEntity<>(actividadService.create(actividadDto), HttpStatus.CREATED);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ActividadDto> getById(@PathVariable Long id) {
        return new ResponseEntity<>(actividadService.getById(id), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    @GetMapping("/page/{page}")
    public ResponseEntity<PageResponse<ActividadDto>> getAll(@PathVariable int page) {
        return new ResponseEntity<>(actividadService.getAll(page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    @GetMapping("/count/clientes/page/{page}")
    public ResponseEntity<PageResponse<ActividadConClientesDto>> getAllWithClientes(@PathVariable int page) {
        return new ResponseEntity<>(actividadConClientesService.getAllActividadesConClientes(page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    @GetMapping("/count/clientes/nombre/{nombre}/page/{page}")
    public ResponseEntity<PageResponse<ActividadConClientesDto>> getAllWithClientes(@PathVariable String nombre, @PathVariable int page) {
        return new ResponseEntity<>(actividadConClientesService.getActividadesConClientesByNombre(nombre,page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    @GetMapping("/{id}/suscripciones/page/{page}")
    public ResponseEntity<PageResponse<SuscripcionConClienteDto>> getSuscripcionesWithClientesPorActividad(@PathVariable Long id, @PathVariable int page) {
        return new ResponseEntity<>(actividadConClientesService.getSuscripcionesConClientesPorActividad(id,page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ActividadDto> update(@PathVariable Long id,@Valid @RequestBody ActividadDto actividadDto) {
        return new ResponseEntity<>(actividadService.update(id, actividadDto), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return new ResponseEntity<>(actividadService.delete(id), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    @GetMapping("/search/{nombre}/page/{page}")
    public ResponseEntity<PageResponse<ActividadDto>> searchByName(@PathVariable int page, @PathVariable String nombre) {
        return new ResponseEntity<>(actividadService.searchByNombre(nombre,page), HttpStatus.OK);
    }

    /*New SCRUM-185 sprint 6*/
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CLIENTE')")
    @GetMapping("/entrenadores/page/{page}")
    public ResponseEntity<PageResponse<ActividadConEntrenadoresDto>> getAllWithEntrenadores(@PathVariable int page) {
        return new ResponseEntity<>(actividadConEntrenadoresService.getAllActividadesConEntrenadores(page), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    @GetMapping("/entrenadores/{id}")
    public ResponseEntity<ActividadConEntrenadoresDto> getByIdWithEntrenadores(@PathVariable Long id) {
        return new ResponseEntity<>(actividadConEntrenadoresService.getByIdWithEntrenadores(id), HttpStatus.OK);
    }
}

