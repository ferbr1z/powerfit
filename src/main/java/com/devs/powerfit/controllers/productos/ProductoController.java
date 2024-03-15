package com.devs.powerfit.controllers.productos;

import com.devs.powerfit.dtos.productos.ProductoDto;
import com.devs.powerfit.interfaces.productos.IProductoService;
import com.devs.powerfit.utils.responses.PageResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    private IProductoService productoService;
    @Autowired
    public ProductoController(IProductoService productoService){
        this.productoService = productoService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @PostMapping
    ResponseEntity<ProductoDto> create(@Valid @RequestBody ProductoDto producto){
        return new ResponseEntity<>(productoService.create(producto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/{id}")
    ResponseEntity<ProductoDto> getById(@PathVariable Long id){
        return new ResponseEntity<>(productoService.getById(id), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/page/{page}")
    ResponseEntity<PageResponse<ProductoDto>> getAll(@PathVariable int page){
        return new ResponseEntity<>(productoService.getAll(page), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @PutMapping("/{id}")
    ResponseEntity<ProductoDto> update(@PathVariable Long id,@Valid @RequestBody ProductoDto productoDto){
        return new ResponseEntity<>(productoService.update(id,productoDto), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @DeleteMapping("/{id}")
    ResponseEntity<Boolean> delete(@PathVariable Long id){
        return new ResponseEntity<>(productoService.delete(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/codigo/{codigo}")
    ResponseEntity<ProductoDto> getByCodigo(@PathVariable String codigo){
        return new ResponseEntity<>(productoService.getByCodigo(codigo), HttpStatus.OK);
    }



}
