package com.devs.powerfit.controllers.cajas;

import com.devs.powerfit.dtos.cajas.CajaDto;
import com.devs.powerfit.dtos.cajas.ExtraccionDeCajaDto;
import com.devs.powerfit.interfaces.cajas.IExtraccionCajaService;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/cajas")
public class ExtraccionesController {
   private final  IExtraccionCajaService extraccionCajaService;

   @Autowired
    public ExtraccionesController(IExtraccionCajaService extraccionCajaService) {
        this.extraccionCajaService = extraccionCajaService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @PostMapping("/extraccion")
    public ResponseEntity<ExtraccionDeCajaDto> create(@RequestBody ExtraccionDeCajaDto extraccionDto) {
        return new ResponseEntity<>(extraccionCajaService.create(extraccionDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/extraccion/{id}")
    public ResponseEntity<ExtraccionDeCajaDto> getById(@PathVariable Long id) {
        return new ResponseEntity<>(extraccionCajaService.getById(id), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/extraccion/page/{page}")
    public ResponseEntity<PageResponse<ExtraccionDeCajaDto>> getByAll(@PathVariable int page) {
        return new ResponseEntity<>(extraccionCajaService.getAll(page), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/extraccion/page/{page}/fechaInicio/{fechaInicio}/fechaFin/{fechaFin}")
    public ResponseEntity<PageResponse<ExtraccionDeCajaDto>> getByAllBetween(
            @PathVariable int page,
            @PathVariable LocalDate fechaInicio,
            @PathVariable LocalDate fechaFin) {
        return new ResponseEntity<>(extraccionCajaService.getAllBetween(page,fechaInicio,fechaFin), HttpStatus.CREATED);
    }



}
