package com.devs.powerfit.controllers.reportes.productos;

import com.devs.powerfit.abstracts.AbstractReportesController;
import com.devs.powerfit.dtos.productos.ProductoDto;
import com.devs.powerfit.interfaces.reportes.productos.IProductosSinStockService;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductosSinStockController extends AbstractReportesController {
    private final IProductosSinStockService productosSinStockService;
    @Autowired
    public ProductosSinStockController(IProductosSinStockService productosSinStockService) {
        this.productosSinStockService = productosSinStockService;
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/productos-sin-stock/page/{page}")
    ResponseEntity<PageResponse<ProductoDto>> getAll(@PathVariable int page){
        return new ResponseEntity<>(productosSinStockService.getAll(page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/cantidad-sin-stock")
    ResponseEntity<Long> getCantidadSinStock(){
        return new ResponseEntity<>(productosSinStockService.getCantidadSinStock(), HttpStatus.OK);
    }



}
