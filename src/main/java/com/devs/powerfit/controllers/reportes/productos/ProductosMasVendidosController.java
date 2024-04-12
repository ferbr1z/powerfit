package com.devs.powerfit.controllers.reportes.productos;

import com.devs.powerfit.abstracts.AbstractReportesController;
import com.devs.powerfit.dtos.reportes.ProductoMasVendidoDTO;
import com.devs.powerfit.interfaces.reportes.productos.IProductosMasVendidosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class ProductosMasVendidosController extends AbstractReportesController {

    private final IProductosMasVendidosService productosMasVendidosService;


    @Autowired
    public ProductosMasVendidosController(IProductosMasVendidosService productosMasVendidosService) {
        this.productosMasVendidosService = productosMasVendidosService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/productos-mas-vendidos-general")
    ResponseEntity<List<ProductoMasVendidoDTO>> getAll(){
        return new ResponseEntity<>(productosMasVendidosService.productosMasVendidosTotal(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/productos-mas-vendidos/{fechaInicio}/{fechaFin}")
    ResponseEntity<List<ProductoMasVendidoDTO>> getAllBetween(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin){
        return new ResponseEntity<>(productosMasVendidosService.productosMasVendidosBetween(fechaInicio, fechaFin), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/productos-mas-vendidos")
    ResponseEntity<List<ProductoMasVendidoDTO>> getAllActual(){
        return new ResponseEntity<>(productosMasVendidosService.productosMasVendidosActual(), HttpStatus.OK);
    }


}
