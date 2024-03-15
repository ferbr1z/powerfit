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
            if(productoDto.getNombre() != null) productoBean.setNombre(productoDto.getNombre());
            if(productoDto.getDescripcion() != null) productoBean.setDescripcion(productoDto.getDescripcion());
            if (productoDto.getCodigo() != null) {
                if (productoDto.getCodigo().length() != 6) {
                    throw new BadRequestException("El código debe tener una longitud de 6 dígitos");
                }
                // Verifica si el código que se está tratando de actualizar ya existe
                Optional<ProductoBean> existingProduct = productoDao.findByCodigoAndActiveIsTrue(productoDto.getCodigo());
                if (existingProduct.isPresent() && !existingProduct.get().getId().equals(id)) {
                    throw new BadRequestException("El código ingresado ya está en uso por otro producto");
                }
                productoBean.setCodigo(productoDto.getCodigo());
            }
            // Validación de costo
            if (productoDto.getCosto() != null) {
                if (productoDto.getCosto() <= 0) {
                    throw new BadRequestException("El costo debe ser mayor que cero");
                }
                productoBean.setCosto(productoDto.getCosto());
            }

            // Validación de precio
            if (productoDto.getPrecio() != null) {
                if (productoDto.getPrecio() <= 0) {
                    throw new BadRequestException("El precio debe ser mayor que cero");
                }
                productoBean.setPrecio(productoDto.getPrecio());
            }

            // Validación de cantidad
            if (productoDto.getCantidad() != null) {
                if (productoDto.getCantidad() < 0) {
                    throw new BadRequestException("La cantidad no puede ser negativa");
                }
                productoBean.setCantidad(productoDto.getCantidad());
            }

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
