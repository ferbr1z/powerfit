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
import static org.mockito.Mockito.when;

class CajaServiceTest {

    @Mock
    private CajaDao cajaDao;
    @Mock
    private CajaMapper cajaMapper;

    @InjectMocks
    private CajaService cajaService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_NullNombre_ThrowsBadRequestException() {
        // Arrange
        CajaDto cajaDto = new CajaDto();
        cajaDto.setMonto(100.0);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> cajaService.create(cajaDto));
    }

    @Test
    void create_NegativeMonto_ThrowsBadRequestException() {
        // Arrange
        CajaDto cajaDto = new CajaDto();
        cajaDto.setNombre("Caja 1");
        cajaDto.setMonto(-100.0);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> cajaService.create(cajaDto));
    }

    @Test
    void create_MaximumCajasCreated_ThrowsBadRequestException() {
        // Arrange
        when(cajaDao.count()).thenReturn(5L);

        CajaDto cajaDto = new CajaDto();
        cajaDto.setNombre("Caja 1");
        cajaDto.setMonto(100.0);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> cajaService.create(cajaDto));
    }

    @Test
    void getById_NonExistingCaja_ThrowsNotFoundException() {
        // Arrange
        Long id = 1L;
        when(cajaDao.findByIdAndActiveTrue(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> cajaService.getById(id));
    }

    @Test
    void getById_ExistingCaja_ReturnsCajaDto() {
        // Arrange
        Long id = 1L;
        CajaDto dtoExistente = new CajaDto();
        dtoExistente.setId(id);
        dtoExistente.setNombre("Caja 1");
        dtoExistente.setMonto(100.0);
        dtoExistente.setActive(true);

        CajaBean cajaExistente = new CajaBean();
        cajaExistente.setId(id);
        cajaExistente.setNombre("Caja 1");
        cajaExistente.setMonto(100.0);
        cajaExistente.setActive(true);

        when((cajaMapper.toDto(cajaExistente))).thenReturn(dtoExistente);
        when(cajaDao.findByIdAndActiveTrue(id)).thenReturn(Optional.of(cajaExistente));

        // Act
        CajaDto cajaDto = cajaService.getById(id);

        // Assert
        assertEquals(cajaExistente.getNombre(), cajaDto.getNombre());
        assertEquals(cajaExistente.getId(), cajaDto.getId());
        assertEquals(cajaExistente.getMonto(), cajaDto.getMonto());
        assertTrue(cajaDto.isActive());
    }



    @Test
    void update_NonExistingCaja_ThrowsNotFoundException() {
        // Arrange
        Long id = 1L;
        CajaDto cajaDto = new CajaDto();
        cajaDto.setMonto(10000.0);
        cajaDto.setNombre("hola");

        when(cajaDao.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> cajaService.update(id, cajaDto));
    }

    @Test
    void delete_NonExistingCaja_ThrowsNotFoundException() {
        // Arrange
        Long id = 1L;
        CajaDto cajaDto = new CajaDto();

        when(cajaDao.findByIdAndActiveTrue(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> cajaService.delete(id));
    }

    @Test
    void delete_ExistingCaja_ReturnsTrue() {
        // Arrange
        Long id = 1L;
        CajaBean eliminado = new CajaBean();
        eliminado.setActive(true);

        when(cajaDao.findByIdAndActiveTrue(id)).thenReturn(Optional.of(eliminado));

        // Act
        boolean result = cajaService.delete(id);

        // Assert
        assertTrue(result);
    }
}
