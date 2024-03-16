package com.devs.powerfit.interfaces.productos;

import com.devs.powerfit.beans.productos.ProductoBean;
import com.devs.powerfit.dtos.productos.ProductoDto;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.utils.responses.PageResponse;

import java.util.Optional;

public interface IProductoService extends IService<ProductoDto> {
    public ProductoDto getByCodigo(String codigo);
    public PageResponse<ProductoDto> searchByNombre(String nombre, int page);

    public PageResponse<ProductoDto> searchByPrecioBetween(Double minPrecio, Double maxPrecio, int page);
}