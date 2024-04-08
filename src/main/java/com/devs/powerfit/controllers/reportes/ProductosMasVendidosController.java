package com.devs.powerfit.controllers.reportes;

import com.devs.powerfit.dtos.reportes.ProductoMasVendidoDTO;
import com.devs.powerfit.interfaces.reportes.IProductosMasVendidosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ProductosMasVendidosController {

    private final IProductosMasVendidosService productosMasVendidosService;


    @Autowired
    public ProductosMasVendidosController(IProductosMasVendidosService productosMasVendidosService) {
        this.productosMasVendidosService = productosMasVendidosService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/productos-mas-vendidos")
    ResponseEntity<List<ProductoMasVendidoDTO>> getAll(){
        return new ResponseEntity<>(productosMasVendidosService.productosMasVendidos(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/productos-mas-vendidos/{fechaInicio}/{fechaFin}")
    ResponseEntity<List<ProductoMasVendidoDTO>> getAllEntreFechas(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaInicio, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaFin){
        return new ResponseEntity<>(productosMasVendidosService.productosMasVendidosBetween(fechaInicio, fechaFin), HttpStatus.OK);
    }


}
