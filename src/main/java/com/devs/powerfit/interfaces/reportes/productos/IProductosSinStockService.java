package com.devs.powerfit.interfaces.reportes.productos;

import com.devs.powerfit.dtos.productos.ProductoDto;
import com.devs.powerfit.dtos.reportes.ProductosSinStockDto;
import com.devs.powerfit.utils.responses.PageResponse;

public interface IProductosSinStockService {

    public PageResponse<ProductoDto> getAll(int page);

    public Long getCantidadSinStock();
}
