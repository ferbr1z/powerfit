package com.devs.powerfit.services.productos;

import com.devs.powerfit.beans.productos.ProductoBean;
import com.devs.powerfit.daos.productos.ProductoDao;
import com.devs.powerfit.dtos.productos.ProductoDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.interfaces.productos.IProductoService;
import com.devs.powerfit.services.auth.AuthService;
import com.devs.powerfit.utils.mappers.productoMapper.ProductoMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ProductoService implements IProductoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductoService.class);
    private ProductoDao productoDao;
    private ProductoMapper productoMapper;
    @Autowired
    public ProductoService(ProductoDao productoDao, ProductoMapper productoMapper){
        this.productoDao = productoDao;
        this.productoMapper = productoMapper;
    }

    @Override
    public ProductoDto create(ProductoDto productoDto) {
        //Se verifican si todos los campos están completados
        if(productoDto.getCodigo().isEmpty() || productoDto.getNombre().isEmpty() || productoDto.getDescripcion().isEmpty() || productoDto.getCosto() == null || productoDto.getPrecio() == null || productoDto.getCantidad() == null){
            throw new BadRequestException("Todos los campos son obligatorio para crear un nuevo producto");
        }
        //Se verifiac que los datos numericos sean positivos y mayor a cero
        if(productoDto.getCantidad() <= 0 || productoDto.getCosto() <= 0 || productoDto.getPrecio() <= 0){
            throw new BadRequestException("El costo, precio y la cantidad no pueden ser menor o igual a 0(cero)");
        }
        //Se verifica que no existe un producto con el mismo codigo
        if (productoDao.findByCodigoAndActiveIsTrue(productoDto.getCodigo()).isPresent()){
            throw new BadRequestException("Ya existe un producto activo con el mismo código");
        }
        //Se verifica que no existe un producto con el mismo nombre
        if (productoDao.findByNombreAndActiveIsTrue(productoDto.getNombre()).isPresent()){
            throw new BadRequestException("Ya existe un producto activo con el mismo nombre");
        }
        ProductoBean producto = productoMapper.toBean(productoDto);
        producto.setActive(true);
        productoDao.save(producto);
        return productoMapper.toDto(producto);
    }

    @Override
    public ProductoDto getById(Long id) {
        return null;
    }

    @Override
    public PageResponse<ProductoDto> getAll(int page) {
        return null;
    }

    @Override
    public ProductoDto update(Long id, ProductoDto productoDto) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
