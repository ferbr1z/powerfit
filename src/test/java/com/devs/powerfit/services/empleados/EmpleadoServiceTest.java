package com.devs.powerfit.services.empleados;

import com.devs.powerfit.beans.empleados.EmpleadoBean;
import com.devs.powerfit.daos.empleados.EmpleadoDao;
import com.devs.powerfit.dtos.empleados.EmpleadoDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.utils.mappers.empleadoMappers.EmpleadoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmpleadoServiceTest {

    @Mock
    private EmpleadoDao empleadoDao;

    @Mock
    private EmpleadoMapper mapper;

    @InjectMocks
    private EmpleadoService empleadoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }



    @Test
    void create_EmpleadoWithExistingEmail_Error() {
        // Arrange
        EmpleadoDto empleadoDto = new EmpleadoDto();
        empleadoDto.setEmail("john@example.com");

        when(empleadoDao.findByEmailAndActiveIsTrue("john@example.com")).thenReturn(Optional.of(new EmpleadoBean()));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> empleadoService.create(empleadoDto));
    }

    @Test
    void create_EmpleadoWithExistingCedula_Error() {
        // Arrange
        EmpleadoDto empleadoDto = new EmpleadoDto();
        empleadoDto.setCedula("1234567890");

        when(empleadoDao.findByCedulaAndActiveIsTrue("1234567890")).thenReturn(Optional.of(new EmpleadoBean()));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> empleadoService.create(empleadoDto));
    }

    @Test
    void getById_NonExistingEmpleado_ThrowsNotFoundException() {
        // Arrange
        Long id = 1L;
        when(empleadoDao.findByIdAndActiveIsTrue(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> empleadoService.getById(id));
    }


    @Test
    void update_NonExistingEmpleado_ThrowsNotFoundException() {
        // Arrange
        Long id = 1L;
        EmpleadoDto empleadoDto = new EmpleadoDto();

        when(empleadoDao.findByIdAndActiveIsTrue(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> empleadoService.update(id, empleadoDto));
    }

    @Test
    void delete_NonExistingEmpleado_ThrowsNotFoundException() {
        // Arrange
        Long id = 1L;

        when(empleadoDao.findByIdAndActiveIsTrue(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> empleadoService.delete(id));
    }

    @Test
    void getByEmail_ExistingEmpleado_ReturnsEmpleadoDto() {

        String email = "john@example.com";
        EmpleadoDto empleadoDtoExpected = new EmpleadoDto();
        empleadoDtoExpected.setId(1L);
        empleadoDtoExpected.setNombre("John Doe");
        empleadoDtoExpected.setEmail(email);
        empleadoDtoExpected.setActive(true);

        // Arrange
        EmpleadoBean empleadoBean = new EmpleadoBean();
        empleadoBean.setEmail(email);
        empleadoBean.setNombre("John Doe");

        when(empleadoDao.findByEmailAndActiveIsTrue(email)).thenReturn(Optional.of(empleadoBean));
        when(mapper.toDto(empleadoBean)).thenReturn(empleadoDtoExpected);

        // Act
        EmpleadoDto empleadoDto = empleadoService.getByEmail(email);

        // Assert
        assertNotNull(empleadoDto);
        assertEquals("john@example.com", empleadoDto.getEmail());
        assertEquals("John Doe", empleadoDto.getNombre());
        assertTrue(empleadoDto.isActive());
    }

    @Test
    void getByEmail_NonExistingEmpleado_ThrowsNotFoundException() {
        // Arrange
        String email = "john@example.com";
        when(empleadoDao.findByEmailAndActiveIsTrue(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> empleadoService.getByEmail(email));
    }
}