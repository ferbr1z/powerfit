package com.devs.powerfit.controllers.empleados;

import com.devs.powerfit.dtos.empleados.EmpleadoDto;
import com.devs.powerfit.interfaces.empleados.IEmpleadoService;
import com.devs.powerfit.utils.responses.PageResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/empleados")
public class EmpleadoController {

    private IEmpleadoService empleadoService;
    @Autowired
    public EmpleadoController(IEmpleadoService empleadoService){
        this.empleadoService = empleadoService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @PostMapping
    ResponseEntity<EmpleadoDto> create (@Valid @RequestBody EmpleadoDto empleadoDto){
        return new ResponseEntity<>(empleadoService.create(empleadoDto), HttpStatus.CREATED);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/{id}")
    ResponseEntity<EmpleadoDto> getById (@PathVariable Long id){
        return new ResponseEntity<>(empleadoService.getById(id), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/page/{page}")
    ResponseEntity<PageResponse<EmpleadoDto>> getAll (@PathVariable int page){
        return new ResponseEntity<>(empleadoService.getAll(page), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @PutMapping("/{id}")
    ResponseEntity<EmpleadoDto> update(@PathVariable Long id,@Valid @RequestBody EmpleadoDto empleadoDto){
        return new ResponseEntity<>(empleadoService.update(id,empleadoDto), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @DeleteMapping("/{id}")
    ResponseEntity<Boolean> delete(@PathVariable Long id){
        return new ResponseEntity<>(empleadoService.delete(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/search/{nombre}/page/{page}")
    ResponseEntity<PageResponse<EmpleadoDto>> searchByNombre(@PathVariable int page, @PathVariable String nombre){
        return new ResponseEntity<>(empleadoService.searchByNombre(nombre,page), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/searchByRol/{id}/page/{page}")
    ResponseEntity<PageResponse<EmpleadoDto>> searchByRolId(@PathVariable int page, @PathVariable Long id){
        return new ResponseEntity<>(empleadoService.searchByRolId(id,page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/getAllEntrenadores/page/{page}")
    ResponseEntity<PageResponse<EmpleadoDto>> getAllEntrenador(@PathVariable int page){
        return new ResponseEntity<>(empleadoService.getAllEntrenadores(page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/getByEmail/{email}")
    ResponseEntity<EmpleadoDto> getByEmail (@PathVariable String email){
        return new ResponseEntity<>(empleadoService.getByEmail(email), HttpStatus.OK);
    }



}
