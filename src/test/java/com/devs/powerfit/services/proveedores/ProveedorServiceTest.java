package com.devs.powerfit.services.proveedores;

import com.devs.powerfit.beans.proveedores.ProveedorBean;
import com.devs.powerfit.daos.proveedores.ProveedorDao;
import com.devs.powerfit.dtos.proveedores.ProveedorDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.utils.mappers.proveedorMapper.ProveedorMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProveedorServiceTest {

    @Mock
    private ProveedorDao proveedorDao;

    @Mock
    private ProveedorMapper mapper;

    @InjectMocks
    private ProveedorService proveedorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void create_NewProveedor_Success() {
        // Arrange
        ProveedorDto proveedorDto = new ProveedorDto();
        proveedorDto.setEmail("new@example.com");

        when(proveedorDao.findByEmailAndActiveIsTrue("new@example.com")).thenReturn(Optional.empty());
        when(mapper.toBean(proveedorDto)).thenReturn(new ProveedorBean());
        when(proveedorDao.save(any(ProveedorBean.class))).thenAnswer(invocation -> {
            ProveedorBean savedProveedor = invocation.getArgument(0);
            savedProveedor.setId(1L);
            return savedProveedor;
        });

        // Act
        ProveedorDto createdProveedor = proveedorService.create(proveedorDto);

        // Assert
        assertNotNull(createdProveedor);
        assertEquals("new@example.com", createdProveedor.getEmail());
    }

    @Test
    void create_ProveedorWithExistingEmail_Error() {
        // Arrange
        ProveedorDto proveedorDto = new ProveedorDto();
        proveedorDto.setEmail("existing@example.com");

        when(proveedorDao.findByEmailAndActiveIsTrue("existing@example.com")).thenReturn(Optional.of(new ProveedorBean()));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> proveedorService.create(proveedorDto));
    }
}