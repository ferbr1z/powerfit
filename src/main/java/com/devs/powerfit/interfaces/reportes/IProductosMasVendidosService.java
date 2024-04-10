package com.devs.powerfit.interfaces.reportes;

import com.devs.powerfit.dtos.reportes.ProductoMasVendidoDTO;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface IProductosMasVendidosService {
//    public List<ProductoMasVendidoDTO> productosMasVendidos();
    public List<ProductoMasVendidoDTO> productosMasVendidosBetween(LocalDate fechaInicio, LocalDate fechaFin );
    public List<ProductoMasVendidoDTO> productosMasVendidosTotal();

    public List<ProductoMasVendidoDTO> productosMasVendidosActual();
}
