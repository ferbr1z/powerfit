package com.devs.powerfit.controllers.mediciones;

import com.devs.powerfit.dtos.mediciones.MedicionDto;
import com.devs.powerfit.dtos.suscripciones.SuscripcionDto;
import com.devs.powerfit.interfaces.mediciones.IMedicionService;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/mediciones")
public class MedicionController {
    private final IMedicionService medicionService;

    @Autowired
    public MedicionController(IMedicionService medicionService) {
        this.medicionService = medicionService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    public ResponseEntity<MedicionDto> create(@RequestBody MedicionDto medicionDto) {
        return new ResponseEntity<>(medicionService.create(medicionDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    public ResponseEntity<MedicionDto> getById(@PathVariable Long id) {
        MedicionDto medicionDto = medicionService.getById(id);
        return new ResponseEntity<>(medicionDto, HttpStatus.OK);
    }

    @GetMapping("/page/{page}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    public ResponseEntity<PageResponse<MedicionDto>> getAll(@PathVariable int page) {
        PageResponse<MedicionDto> medicionesPage = medicionService.getAll(page);
        return new ResponseEntity<>(medicionesPage, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    public ResponseEntity<MedicionDto> update(@PathVariable Long id, @RequestBody MedicionDto medicionDto) {
        MedicionDto updatedMedicion = medicionService.update(id, medicionDto);
        return new ResponseEntity<>(updatedMedicion, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        boolean deleted = medicionService.delete(id);
        return new ResponseEntity<>(deleted, HttpStatus.OK);
    }

    @GetMapping("/search/{nombre}/page/{page}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    public ResponseEntity<PageResponse<MedicionDto>> searchByName(@PathVariable int page, @PathVariable String nombre) {
        return new ResponseEntity<>(medicionService.searchByNombreCliente(nombre,page), HttpStatus.OK);
    }

    @GetMapping("/search/{ci}/page/{page}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    public ResponseEntity<PageResponse<MedicionDto>> searchByCi(@PathVariable int page, @PathVariable int ci) {
        return new ResponseEntity<>(medicionService.searchByCiCliente(ci,page), HttpStatus.OK);
    }

    @GetMapping("/searchByClienteId/{id}/page/{page}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    public ResponseEntity<PageResponse<MedicionDto>> searchById(@PathVariable int page, @PathVariable Long id) {
        return new ResponseEntity<>(medicionService.searchByIdCliente(id,page), HttpStatus.OK);
    }

    @GetMapping("/mis-mediciones/page/{page}")
    public ResponseEntity<PageResponse<MedicionDto>> getAllMisMediciones(@PathVariable int page, Principal principal){
        return new ResponseEntity<>(medicionService.searchByClienteEmail(principal.getName(),page), HttpStatus.OK);

    }

}
