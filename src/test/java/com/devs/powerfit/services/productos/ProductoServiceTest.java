package com.devs.powerfit.services.productos;

import com.devs.powerfit.beans.productos.ProductoBean;
import com.devs.powerfit.daos.productos.ProductoDao;
import com.devs.powerfit.dtos.productos.ProductoDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.utils.mappers.productoMapper.ProductoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ProductoServiceTest {

    @Mock
    private ProductoDao productoDao;

    @Mock
    private ProductoMapper productoMapper;

    @InjectMocks
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void create_NewProduct_Success() {
        // Configuración del mock
        ProductoDto productoDto = new ProductoDto();
        productoDto.setCodigo("234232");
        productoDto.setNombre("Producto 1");
        productoDto.setCosto(10.0);
        productoDto.setPrecio(20.0);
        productoDto.setCantidad(50);

        ProductoBean productoBean = new ProductoBean();
        productoMapper.toBean(productoDto);
        productoBean.setId(1L);
        productoBean.setCodigo("234232");
        productoBean.setNombre("Producto 1");
        productoBean.setCosto(10.0);
        productoBean.setPrecio(20.0);
        productoBean.setCantidad(50);
        productoBean.setActive(true);

        when(productoDao.findByCodigo("234232")).thenReturn(Optional.empty());
        when(productoMapper.toBean(productoDto)).thenReturn(productoBean);
        when(productoDao.save(any(ProductoBean.class))).thenReturn(productoBean);
        when(productoMapper.toDto(productoBean)).thenReturn(productoDto);

        ProductoDto createdProduct = productoService.create(productoDto);

        assertNotNull(createdProduct);
        assertEquals("234232", createdProduct.getCodigo());
        assertEquals("Producto 1", createdProduct.getNombre());
        assertEquals(10.0, createdProduct.getCosto());
        assertEquals(20.0, createdProduct.getPrecio());
        assertEquals(50, createdProduct.getCantidad());
    }

    @Test
    void create_ProductWithExistingCode_ActivateProduct_Success() {
        // Configuración del mock
        ProductoDto productoDto = new ProductoDto();
        productoDto.setCodigo("234232");
        productoDto.setNombre("Producto 1");
        productoDto.setCosto(10.0);
        productoDto.setPrecio(20.0);
        productoDto.setCantidad(50);

        ProductoBean existingProductBean = new ProductoBean();
        existingProductBean.setId(1L);
        existingProductBean.setCodigo("234232");
        existingProductBean.setNombre("Producto Existente");
        existingProductBean.setCosto(5.0);
        existingProductBean.setPrecio(15.0);
        existingProductBean.setCantidad(20);
        existingProductBean.setActive(false);

        when(productoDao.findByCodigo("234232")).thenReturn(Optional.of(existingProductBean));
        when(productoDao.save(existingProductBean)).thenReturn(existingProductBean);
        when(productoMapper.toDto(existingProductBean)).thenReturn(productoDto);

        ProductoDto createdProduct = productoService.create(productoDto);

        assertNotNull(createdProduct);
        assertEquals("234232", createdProduct.getCodigo());
        assertEquals("Producto 1", createdProduct.getNombre());
        assertEquals(10.0, createdProduct.getCosto());
        assertEquals(20.0, createdProduct.getPrecio());
        assertEquals(50, createdProduct.getCantidad());
    }

    @Test
    void create_ProductWithExistingActiveCode_Error() {
        // Configuración del mock
        ProductoDto productoDto = new ProductoDto();
        productoDto.setCodigo("234232");
        productoDto.setNombre("Producto 1");
        productoDto.setCosto(10.0);
        productoDto.setPrecio(20.0);
        productoDto.setCantidad(50);

        ProductoBean existingProductBean = new ProductoBean();
        existingProductBean.setId(1L);
        existingProductBean.setCodigo("234232");
        existingProductBean.setNombre("Producto Existente");
        existingProductBean.setCosto(5.0);
        existingProductBean.setPrecio(15.0);
        existingProductBean.setCantidad(20);
        existingProductBean.setActive(true);

        when(productoDao.findByCodigo("234232")).thenReturn(Optional.of(existingProductBean));

        assertThrows(BadRequestException.class, () -> productoService.create(productoDto));
    }

    @Test
    void getById_ExistingProduct_ReturnsProductDto() {
        // Arrange
        Long productId = 1L;
        ProductoDto expectedProductDto = new ProductoDto();
        expectedProductDto.setId(productId);
        expectedProductDto.setCodigo("123456");
        expectedProductDto.setNombre("Producto de prueba");
        expectedProductDto.setCosto(10.0);
        expectedProductDto.setPrecio(20.0);
        expectedProductDto.setCantidad(50);

        ProductoBean existingProductBean = new ProductoBean();
        existingProductBean.setId(productId);
        existingProductBean.setCodigo("123456");
        existingProductBean.setNombre("Producto de prueba");
        existingProductBean.setCosto(10.0);
        existingProductBean.setPrecio(20.0);
        existingProductBean.setCantidad(50);

        when(productoDao.findByIdAndActiveIsTrue(productId)).thenReturn(Optional.of(existingProductBean));
        when(productoMapper.toDto(existingProductBean)).thenReturn(expectedProductDto);

        // Act
        ProductoDto actualProductDto = productoService.getById(productId);

        // Assert
        assertEquals(expectedProductDto, actualProductDto);
    }

    @Test
    void getById_NonExistingProduct_ThrowsNotFoundException() {
        // Arrange
        Long productId = 1L;

        when(productoDao.findByIdAndActiveIsTrue(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> productoService.getById(productId));
    }

    @Test
    void update_ExistingProduct_ReturnsUpdatedProductDto() {
        // Arrange
        Long productId = 1L;
        ProductoDto inputProductDto = new ProductoDto();
        inputProductDto.setNombre("Nuevo Nombre");
        inputProductDto.setDescripcion("Nueva Descripción");
        inputProductDto.setCodigo("123");
        inputProductDto.setCosto(15.0);
        inputProductDto.setPrecio(25.0);
        inputProductDto.setCantidad(60);

        ProductoBean existingProductBean = new ProductoBean();
        existingProductBean.setId(productId);
        existingProductBean.setNombre("Producto Existente");
        existingProductBean.setDescripcion("Descripción Existente");
        existingProductBean.setCodigo("123");
        existingProductBean.setCosto(10.0);
        existingProductBean.setPrecio(20.0);
        existingProductBean.setCantidad(50);

        ProductoDto expectedUpdatedProductDto = new ProductoDto();
        expectedUpdatedProductDto.setId(productId);
        expectedUpdatedProductDto.setNombre("Nuevo Nombre");
        expectedUpdatedProductDto.setDescripcion("Nueva Descripción");
        expectedUpdatedProductDto.setCodigo("123");
        expectedUpdatedProductDto.setCosto(15.0);
        expectedUpdatedProductDto.setPrecio(25.0);
        expectedUpdatedProductDto.setCantidad(60);

        when(productoDao.findByIdAndActiveIsTrue(productId)).thenReturn(Optional.of(existingProductBean));
        when(productoDao.save(existingProductBean)).thenReturn(existingProductBean);
        when(productoMapper.toDto(existingProductBean)).thenReturn(expectedUpdatedProductDto);

        // Act
        ProductoDto actualUpdatedProductDto = productoService.update(productId, inputProductDto);

        // Assert
        assertEquals(expectedUpdatedProductDto, actualUpdatedProductDto);
    }

    @Test
    void update_NonExistingProduct_ThrowsNotFoundException() {
        // Arrange
        Long productId = 1L;
        ProductoDto inputProductDto = new ProductoDto();

        when(productoDao.findByIdAndActiveIsTrue(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> productoService.update(productId, inputProductDto));
    }

    @Test
    void delete_ExistingProduct_ReturnsTrue() {
        // Arrange
        Long productId = 1L;
        ProductoBean existingProductBean = new ProductoBean();
        existingProductBean.setId(productId);
        existingProductBean.setActive(true);

        when(productoDao.findByIdAndActiveIsTrue(productId)).thenReturn(Optional.of(existingProductBean));
        when(productoDao.save(existingProductBean)).thenReturn(existingProductBean);

        // Act
        boolean result = productoService.delete(productId);

        // Assert
        assertTrue(result);
    }

    @Test
    void delete_NonExistingProduct_ThrowsNotFoundException() {
        // Arrange
        Long productId = 1L;

        when(productoDao.findByIdAndActiveIsTrue(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> productoService.delete(productId));
    }

}
