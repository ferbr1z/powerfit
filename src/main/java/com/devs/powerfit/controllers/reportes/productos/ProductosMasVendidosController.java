package com.devs.powerfit.controllers.reportes.productos;

import com.devs.powerfit.abstracts.AbstractReportesController;
import com.devs.powerfit.dtos.productos.ProductoDto;
import com.devs.powerfit.dtos.reportes.productos.ProductoMasVendidoDTO;
import com.devs.powerfit.interfaces.reportes.productos.IProductoReportesService;
import com.devs.powerfit.utils.responses.PageResponse;
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

    private final IProductoReportesService productoReportesService;


    @Autowired
    public ProductosMasVendidosController(IProductoReportesService productosMasVendidosService) {
        this.productoReportesService = productosMasVendidosService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/productos-mas-vendidos-general")
    ResponseEntity<List<ProductoMasVendidoDTO>> getAll(){
        return new ResponseEntity<>(productoReportesService.productosMasVendidosTotal(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/productos-mas-vendidos/{fechaInicio}/{fechaFin}")
    ResponseEntity<List<ProductoMasVendidoDTO>> getAllBetween(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin){
        return new ResponseEntity<>(productoReportesService.productosMasVendidosBetween(fechaInicio, fechaFin), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/productos-mas-vendidos")
    ResponseEntity<List<ProductoMasVendidoDTO>> getAllActual(){
        return new ResponseEntity<>(productoReportesService.productosMasVendidosActual(), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/productos-sin-stock/page/{page}")
    ResponseEntity<PageResponse<ProductoDto>> getAll(@PathVariable int page){
        return new ResponseEntity<>(productoReportesService.getAll(page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/cantidad-sin-stock")
    ResponseEntity<Long> getCantidadSinStock(){
        return new ResponseEntity<>(productoReportesService.getCantidadSinStock(), HttpStatus.OK);
    }


}
