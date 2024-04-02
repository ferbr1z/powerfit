package com.devs.powerfit.controllers.proveedores;


import com.devs.powerfit.dtos.proveedores.ProveedorDto;
import com.devs.powerfit.interfaces.proveedores.IProveedorService;
import com.devs.powerfit.utils.responses.PageResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/proveedores")
public class ProveedorController {
    private IProveedorService proveedorService;

    @Autowired
    public ProveedorController(IProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @PostMapping
    ResponseEntity<ProveedorDto> create(@Valid @RequestBody ProveedorDto proveedor){
        return new ResponseEntity<>(proveedorService.create(proveedor), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/{id}")
    ResponseEntity<ProveedorDto> getById(@Valid @PathVariable Long id){
        return new ResponseEntity<>(proveedorService.getById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/page/{page}")
    ResponseEntity<PageResponse<ProveedorDto>> getAll(@PathVariable int page){
        return new ResponseEntity<>(proveedorService.getAll(page), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @PutMapping("/{id}")
    ResponseEntity<ProveedorDto> update(@PathVariable Long id, @Valid @RequestBody ProveedorDto proveedor){
        return new ResponseEntity<>(proveedorService.update(id,proveedor), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @DeleteMapping("/{id}")
    ResponseEntity<Boolean> delete(@PathVariable Long id){
        return new ResponseEntity<>(proveedorService.delete(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/search/{nombre}/page/{page}")
    ResponseEntity<PageResponse<ProveedorDto>> searchByNombre(@PathVariable int page, @PathVariable String nombre){
        return new ResponseEntity<>(proveedorService.searchByNombre(nombre,page), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/searchByRuc/{ruc}/page/{page}")
    ResponseEntity<PageResponse<ProveedorDto>> searchByRuc(@PathVariable int page, @PathVariable String ruc){
        return new ResponseEntity<>(proveedorService.searchByRuc(ruc,page), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/getByRuc/{ruc}")
    ResponseEntity<ProveedorDto> getByRuc(@PathVariable String ruc){
        return new ResponseEntity<>(proveedorService.getByRuc(ruc), HttpStatus.OK);
    }


}
