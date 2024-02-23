package com.devs.powerfit.controllers;

import com.devs.powerfit.dtos.ActividadDto;
import com.devs.powerfit.dtos.PageResponse;
import com.devs.powerfit.interfaces.IActividadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/actividades")
public class ActividadController {

    private final IActividadService actividadService;
    @Autowired
    public ActividadController(IActividadService actividadService) {
        this.actividadService = actividadService;
    }

    @PostMapping
    public ResponseEntity<ActividadDto> create(@RequestBody ActividadDto actividadDto) {
        return new ResponseEntity<>(actividadService.create(actividadDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActividadDto> getById(@PathVariable Long id) {
        return new ResponseEntity<>(actividadService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/page/{page}")
    public ResponseEntity<PageResponse<ActividadDto>> getAll(@PathVariable int page) {
        return new ResponseEntity<>(actividadService.getAll(page), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActividadDto> update(@PathVariable Long id, @RequestBody ActividadDto actividadDto) {
        return new ResponseEntity<>(actividadService.update(id, actividadDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return new ResponseEntity<>(actividadService.delete(id), HttpStatus.OK);
    }
}

