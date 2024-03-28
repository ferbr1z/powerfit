package com.devs.powerfit.controllers.arqueo;

import com.devs.powerfit.dtos.arqueo.ArqueoConDetallesDto;
import com.devs.powerfit.dtos.arqueo.ArqueoDto;
import com.devs.powerfit.interfaces.arqueo.IArqueoService;
import com.devs.powerfit.services.arqueo.ArqueoConDetallesService;
import com.devs.powerfit.services.arqueo.ArqueoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/arqueo")
public class ArqueoController {
    @Autowired
    public ArqueoController(ArqueoService arqueoService) {
        this.arqueoService = arqueoService;
    }

    private ArqueoService arqueoService;

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @PostMapping("/{id}")
    public ResponseEntity<ArqueoDto> generarArqueo(@PathVariable Long id) {
        return new ResponseEntity<>(arqueoService.realizarArqueo(id), HttpStatus.CREATED);
    }


}
