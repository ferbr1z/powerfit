package com.devs.powerfit.controllers.arqueo;


import com.devs.powerfit.dtos.arqueo.ArqueoDto;
import com.devs.powerfit.dtos.arqueo.ArqueoRequestDto;
import com.devs.powerfit.interfaces.arqueo.IArqueoService;
import com.devs.powerfit.utils.responses.PageResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/arqueo")
public class ArqueoController {
    @Autowired
    public ArqueoController(IArqueoService arqueoService) {
        this.arqueoService = arqueoService;
    }

    private IArqueoService arqueoService;

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @PostMapping()
    ResponseEntity<ArqueoDto> generarArqueo(@RequestBody @Valid ArqueoRequestDto arqueoRequestDto) {
        return new ResponseEntity<>(arqueoService.realizarArqueo(arqueoRequestDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/page/{page}")
    ResponseEntity<PageResponse<ArqueoDto>> getAll(@PathVariable int page) {
        return new ResponseEntity<>(arqueoService.getAll(page), HttpStatus.OK);
    }


    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/searchByFecha/{fecha}/page/{page}")
    ResponseEntity<PageResponse<ArqueoDto>> getAll(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fecha, @PathVariable int page) {
        return new ResponseEntity<>(arqueoService.getAllByFecha(fecha, page), HttpStatus.OK);
    }



}
