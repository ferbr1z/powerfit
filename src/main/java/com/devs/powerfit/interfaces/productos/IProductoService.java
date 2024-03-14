package com.devs.powerfit.interfaces.productos;

import com.devs.powerfit.beans.productos.ProductoBean;
import com.devs.powerfit.dtos.productos.ProductoDto;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.bases.IService;

import java.util.Optional;

public interface IProductoService extends IService<ProductoDto> {
    public ProductoDto getByCodigo(String codigo);
}