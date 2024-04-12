package com.devs.powerfit.services.reportes.productos;

import com.devs.powerfit.beans.productos.ProductoBean;
import com.devs.powerfit.daos.productos.ProductoDao;
import com.devs.powerfit.dtos.productos.ProductoDto;

import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.reportes.productos.IProductosSinStockService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.productoMapper.ProductoMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductosSinStockService implements IProductosSinStockService {

    private final ProductoDao productoDao;
    private final ProductoMapper mapper;

    @Autowired
    public ProductosSinStockService(ProductoDao productoDao, ProductoMapper mapper) {
        this.productoDao = productoDao;
        this.mapper = mapper;
    }

    @Override
    public PageResponse<ProductoDto> getAll(int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        Page productos = productoDao.findAllByCantidadAndActiveIsTrue(pag, 0);
        if (productos.isEmpty()){
            throw new NotFoundException("No hay productos sin Stock");
        }
        var productosDto = productos.map(producto ->
                mapper.toDto((ProductoBean) producto));
        return new PageResponse<ProductoDto>(productosDto.getContent(),
                productosDto.getTotalPages(),
                productosDto.getTotalElements(),
                productosDto.getNumber() + 1);
    }

    @Override
    public Long getCantidadSinStock(){
        return productoDao.countByCantidadAndActiveIsTrue(0);
    }


}
