package com.devs.powerfit.controllers.tiposDePago;

import com.devs.powerfit.dtos.tiposDePagos.TipoDePagoDto;
import com.devs.powerfit.interfaces.tiposDePago.ITipoDePagoService;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tipos-de-pago")
public class TipoDePagoController {

    private final ITipoDePagoService tipoDePagoService;

    @Autowired
    public TipoDePagoController(ITipoDePagoService tipoDePagoService) {
        this.tipoDePagoService = tipoDePagoService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @PostMapping
    public ResponseEntity<TipoDePagoDto> create(@RequestBody TipoDePagoDto tipoDePagoDto) {
        return new ResponseEntity<>(tipoDePagoService.create(tipoDePagoDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/{id}")
    public ResponseEntity<TipoDePagoDto> getById(@PathVariable Long id) {
        return new ResponseEntity<>(tipoDePagoService.getById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/page/{page}")
    public ResponseEntity<PageResponse<TipoDePagoDto>> getAll(@PathVariable int page) {
        return new ResponseEntity<>(tipoDePagoService.getAll(page), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @PutMapping("/{id}")
    public ResponseEntity<TipoDePagoDto> update(@PathVariable Long id, @RequestBody TipoDePagoDto tipoDePagoDto) {
        return new ResponseEntity<>(tipoDePagoService.update(id, tipoDePagoDto), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return new ResponseEntity<>(tipoDePagoService.delete(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/search/{nombre}/page/{page}")
    public ResponseEntity<PageResponse<TipoDePagoDto>> searchByName(@PathVariable int page, @PathVariable String nombre) {
        return new ResponseEntity<>(tipoDePagoService.searchByNombre(nombre, page), HttpStatus.OK);
    }
}
