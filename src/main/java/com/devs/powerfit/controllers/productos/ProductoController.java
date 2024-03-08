package com.devs.powerfit.controllers.productos;

import com.devs.powerfit.dtos.productos.ProductoDto;
import com.devs.powerfit.interfaces.productos.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    private IProductoService productoService;
    @Autowired
    public ProductoController(IProductoService productoService){
        this.productoService = productoService;
    }
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    ResponseEntity<ProductoDto> create(@RequestBody ProductoDto producto){
        return new ResponseEntity<>(productoService.create(producto), HttpStatus.CREATED);
    }

}
