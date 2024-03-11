package com.devs.powerfit.services.cajas;

import com.devs.powerfit.beans.cajas.CajaBean;
import com.devs.powerfit.daos.cajas.CajaDao;
import com.devs.powerfit.dtos.cajas.CajaDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.utils.mappers.CajaMappers.CajaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CajaServiceTest {

    @Mock
    private CajaDao cajaDao;

    @Mock
    private CajaMapper mapper;

    @InjectMocks
    private CajaService cajaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createCaja_WithValidData_ShouldCreateCaja() {
        // Arrange
        CajaDto cajaDto = new CajaDto();
        cajaDto.setNombre("Caja1");
        cajaDto.setMonto(100.0);

        when(cajaDao.existsByNombreAndActiveTrue("Caja1")).thenReturn(false);
        when(mapper.toBean(cajaDto)).thenReturn(new CajaBean());
        when(cajaDao.save(any())).thenReturn(new CajaBean());
        when(mapper.toDto(any())).thenReturn(cajaDto);

        // Act
        CajaDto result = cajaService.create(cajaDto);

        // Assert

        assertNotNull(result);
        assertEquals("Caja1", result.getNombre());
        assertEquals(100.0, result.getMonto());
    }


    @Test
    void createCaja_WithExistingNombre_ShouldThrowBadRequestException() {
        // Arrange
        CajaDto cajaDto = new CajaDto();
        cajaDto.setNombre("Caja1");
        cajaDto.setMonto(100.0);

        when(cajaDao.existsByNombreAndActiveTrue("Caja1")).thenReturn(true);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> cajaService.create(cajaDto));
    }

    @Test
    void createCaja_WithNegativeMonto_ShouldThrowBadRequestException() {
        // Arrange
        CajaDto cajaDto = new CajaDto();
        cajaDto.setNombre("Caja1");
        cajaDto.setMonto(-100.0);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> cajaService.create(cajaDto));
    }

    @Test
    void getById_WithValidId_ShouldReturnCajaDto() {
        // Arrange
        Long id = 1L;
        CajaBean caja = new CajaBean();
        caja.setId(id);
        caja.setNombre("Caja1");
        caja.setMonto(100.0);
        when(cajaDao.findByIdAndActiveTrue(id)).thenReturn(Optional.of(caja));
        CajaDto cajaDto = new CajaDto();
        cajaDto.setId(id);
        cajaDto.setNombre("Caja1");
        cajaDto.setMonto(100.0);
        when(mapper.toDto(caja)).thenReturn(cajaDto);

        // Act
        CajaDto result = cajaService.getById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Caja1", result.getNombre());
        assertEquals(100.0, result.getMonto());
    }

    @Test
    void getById_WithInvalidId_ShouldThrowNotFoundException() {
        // Arrange
        Long id = 1L;
        when(cajaDao.findByIdAndActiveTrue(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> cajaService.getById(id));
    }

    @Test
    void updateCaja_WithValidIdAndData_ShouldUpdateCaja() {
        // Arrange
        Long id = 1L;
        CajaDto cajaDto = new CajaDto();
        cajaDto.setNombre("Caja1");
        cajaDto.setMonto(200.0);
        CajaBean caja = new CajaBean();
        caja.setId(id);
        caja.setNombre("Caja1");
        caja.setMonto(100.0);
        when(cajaDao.findByIdAndActiveTrue(id)).thenReturn(Optional.of(caja));
        when(cajaDao.existsByNombreAndActiveTrue("Caja1")).thenReturn(false);
        when(mapper.toBean(cajaDto)).thenReturn(caja);
        when(cajaDao.save(any())).thenReturn(caja);
        when(mapper.toDto(caja)).thenReturn(cajaDto);

        // Act
        CajaDto result = cajaService.update(id, cajaDto);

        // Assert
        assertNotNull(result);
        assertEquals("Caja1", result.getNombre());
        assertEquals(200.0, result.getMonto());
    }

    @Test
    void updateCaja_WithInvalidId_ShouldThrowNotFoundException() {
        // Arrange
        Long id = 1L;
        CajaDto cajaDto = new CajaDto();
        cajaDto.setNombre("Caja1");
        cajaDto.setMonto(200.0);
        when(cajaDao.findByIdAndActiveTrue(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> cajaService.update(id, cajaDto));
    }

    @Test
    void deleteCaja_WithValidId_ShouldDeleteCaja() {
        // Arrange
        Long id = 1L;
        CajaBean caja = new CajaBean();
        caja.setId(id);
        caja.setNombre("Caja1");
        caja.setMonto(100.0);
        when(cajaDao.findByIdAndActiveTrue(id)).thenReturn(Optional.of(caja));
        when(cajaDao.save(caja)).thenReturn(caja);

        // Act
        boolean result = cajaService.delete(id);

        // Assert
        assertTrue(result);
        assertFalse(caja.isActive());
    }

    @Test
    void deleteCaja_WithInvalidId_ShouldThrowNotFoundException() {
        // Arrange
        Long id = 1L;
        when(cajaDao.findByIdAndActiveTrue(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> cajaService.delete(id));
    }
}
