package com.devs.powerfit.services.productos;

import com.devs.powerfit.beans.productos.ProductoBean;
import com.devs.powerfit.daos.productos.ProductoDao;
import com.devs.powerfit.dtos.productos.ProductoDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.productos.IProductoService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.productoMapper.ProductoMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ProductoService implements IProductoService {
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
        //Se verifica que los datos numericos sean positivos y mayor a cero
        if(productoDto.getCantidad() <= 0 || productoDto.getCosto() <= 0 || productoDto.getPrecio() <= 0){
            throw new BadRequestException("El costo, precio y la cantidad no pueden ser menor o igual a 0(cero)");
        }
        //Se verifica que no existe un producto con el mismo codigo
        if (productoDao.findByCodigoAndActiveIsTrue(productoDto.getCodigo()).isPresent()){
            throw new BadRequestException("Ya existe un producto activo con el mismo código");
        }


        ProductoBean producto = productoMapper.toBean(productoDto);
        producto.setActive(true);
        productoDao.save(producto);
        return productoMapper.toDto(producto);
    }

    @Override
    public ProductoDto getById(Long id) {
        Optional<ProductoBean> producto = productoDao.findByIdAndActiveIsTrue(id);
        if(producto.isPresent()){
            return productoMapper.toDto(producto.get());
        }else {
            throw new NotFoundException("Producto no encontrado");
        }
    }

    @Override
    public PageResponse<ProductoDto> getAll(int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        Page<ProductoBean> productos = productoDao.findAllByActiveIsTrue(pag);

        if (productos.isEmpty()){
            throw new NotFoundException("No hay productos en la lista");
        }
        var productosDto = productos.map(producto -> productoMapper.toDto(producto));

        var pageResponse = new PageResponse<ProductoDto>(productosDto.getContent(),
                productosDto.getTotalPages(),
                productosDto.getTotalElements(),
                productosDto.getNumber() + 1);

        return pageResponse;
    }

    @Override
    public ProductoDto update(Long id, ProductoDto productoDto) {

        Optional<ProductoBean> producto = productoDao.findByIdAndActiveIsTrue(id);
        if (producto.isPresent()) {
            ProductoBean productoBean = producto.get();
            productoBean.setNombre(productoDto.getNombre() != null ? productoDto.getNombre() : productoBean.getNombre());
            productoBean.setDescripcion(productoDto.getDescripcion() != null ? productoDto.getDescripcion() : productoBean.getDescripcion());
            productoBean.setCodigo(productoDto.getCodigo() != null ? productoDto.getCodigo() : productoBean.getCodigo());
            productoBean.setPrecio(productoDto.getPrecio() != null ? productoDto.getPrecio() : productoBean.getPrecio());
            productoBean.setCosto(productoDto.getCosto() != null ? productoDto.getCosto() : productoBean.getCosto());
            productoBean.setCantidad(productoDto.getCantidad() != null ? productoDto.getCantidad() : productoBean.getCantidad());
            productoDao.save(productoBean);
            return productoMapper.toDto(productoBean);
        }
        throw new NotFoundException("Producto no encontrado");
    }

    @Override
    public boolean delete(Long id) {
        Optional<ProductoBean> producto = productoDao.findByIdAndActiveIsTrue(id);
        if (producto.isPresent()){
            ProductoBean productoBean = producto.get();
            productoBean.setActive(false);
            productoDao.save(productoBean);
            return true;
        }
        throw new NotFoundException("Producto no encontrado");
    }

    @Override
    public ProductoDto getByCodigo(String codigo) {
        Optional<ProductoBean> producto = productoDao.findByCodigoAndActiveIsTrue(codigo);
        if(producto.isPresent()){
            return productoMapper.toDto(producto.get());
        }else {
            throw new NotFoundException("Producto no encontrado");
        }
    }
}
