package com.devs.powerfit.controllers.arqueo;

import com.devs.powerfit.dtos.arqueo.ArqueoDto;
import com.devs.powerfit.interfaces.arqueo.IArqueoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/arqueo")
public class ArqueoController {
    @Autowired
    public ArqueoController(IArqueoService arqueoService) {
        this.arqueoService = arqueoService;
    }

    private IArqueoService arqueoService;

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @PostMapping("/generar")
    public ResponseEntity<ArqueoDto> generarArqueo(@RequestBody ArqueoDto arqueoDto) {
        ArqueoDto arqueoGenerado = arqueoService.generarArqueo(arqueoDto);
        return new ResponseEntity<>(arqueoGenerado, HttpStatus.CREATED);
    }


}
