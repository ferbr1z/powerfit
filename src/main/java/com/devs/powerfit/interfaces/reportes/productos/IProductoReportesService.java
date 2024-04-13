package com.devs.powerfit.interfaces.reportes.productos;

import com.devs.powerfit.dtos.productos.ProductoDto;
import com.devs.powerfit.dtos.reportes.productos.ProductoMasVendidoDTO;
import com.devs.powerfit.utils.responses.PageResponse;

import java.time.LocalDate;
import java.util.List;

public interface IProductoReportesService {
//    public List<ProductoMasVendidoDTO> productosMasVendidos();
    public List<ProductoMasVendidoDTO> productosMasVendidosBetween(LocalDate fechaInicio, LocalDate fechaFin );
    public List<ProductoMasVendidoDTO> productosMasVendidosTotal();
    public List<ProductoMasVendidoDTO> productosMasVendidosActual();

    public PageResponse<ProductoDto> getAll(int page);

    public Long getCantidadSinStock();
}
