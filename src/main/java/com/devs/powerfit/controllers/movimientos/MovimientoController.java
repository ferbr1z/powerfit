package com.devs.powerfit.controllers.movimientos;

import com.devs.powerfit.dtos.movimientos.MovimientoConDetalleDto;
import com.devs.powerfit.services.movimientos.MovimientoConDetalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {
    private final MovimientoConDetalleService service;
    @Autowired
    public MovimientoController(MovimientoConDetalleService service) {
        this.service = service;
    }
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    public ResponseEntity<MovimientoConDetalleDto> create(@RequestBody MovimientoConDetalleDto movimientoConDetalleDto) {
        return new ResponseEntity<>(service.create(movimientoConDetalleDto), HttpStatus.CREATED);
    }
}
