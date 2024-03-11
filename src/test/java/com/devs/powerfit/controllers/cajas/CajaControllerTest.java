package com.devs.powerfit.controllers.cajas;

import com.devs.powerfit.dtos.cajas.CajaDto;
import com.devs.powerfit.interfaces.cajas.ICajaService;
import com.devs.powerfit.utils.responses.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

public class CajaControllerTest {

    @Mock
    private ICajaService cajaService;

    @InjectMocks
    private CajaController cajaController;

    @BeforeEach
    public void setUp() {
        cajaService = mock(ICajaService.class);
        cajaController = new CajaController(cajaService, sesionCajaService);
    }

    @Test
    public void create_CajaDtoGiven_ShouldReturnCreatedResponse() {
        // Arrange
        CajaDto cajaDto = new CajaDto();
        when(cajaService.create(cajaDto)).thenReturn(cajaDto);

        // Act
        ResponseEntity<CajaDto> response = cajaController.create(cajaDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(cajaDto, response.getBody());
    }

    @Test
    public void getById_ValidIdGiven_ShouldReturnCajaDto() {
        // Arrange
        Long id = 1L;
        CajaDto cajaDto = new CajaDto();
        when(cajaService.getById(id)).thenReturn(cajaDto);

        // Act
        ResponseEntity<CajaDto> response = cajaController.getById(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cajaDto, response.getBody());
    }
    @Test
    public void getAll_ValidPageNumberGiven_ShouldReturnPageResponseOfCajaDto() {
        // Arrange
        int page = 1;
        List<CajaDto> cajas = new ArrayList<>(); // lista vacía
        int totalPages = 1; // solo una página
        long totalElements = 0; // no hay elementos
        PageResponse<CajaDto> pageResponse = new PageResponse<>(cajas, totalPages, totalElements, page);
        when(cajaService.getAll(page)).thenReturn(pageResponse);

// Act
        ResponseEntity<PageResponse<CajaDto>> response = cajaController.getAll(page);

// Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pageResponse, response.getBody());

    }

    @Test
    public void update_ValidIdAndCajaDtoGiven_ShouldReturnUpdatedCajaDto() {
        // Arrange
        Long id = 1L;
        CajaDto cajaDto = new CajaDto();
        when(cajaService.update(id, cajaDto)).thenReturn(cajaDto);

        // Act
        ResponseEntity<CajaDto> response = cajaController.update(id, cajaDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cajaDto, response.getBody());
    }

    @Test
    public void delete_ValidIdGiven_ShouldReturnTrue() {
        // Arrange
        Long id = 1L;
        when(cajaService.delete(id)).thenReturn(true);

        // Act
        ResponseEntity<Boolean> response = cajaController.delete(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
