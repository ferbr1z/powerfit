package com.devs.powerfit.services.cajas;

import com.devs.powerfit.beans.auth.UsuarioBean;
import com.devs.powerfit.beans.cajas.CajaBean;
import com.devs.powerfit.daos.auth.UsuarioDao;
import com.devs.powerfit.daos.cajas.CajaDao;
import com.devs.powerfit.dtos.cajas.SesionCajaDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.daos.cajas.SesionCajaDao;
import com.devs.powerfit.services.cajas.SesionCajaService;
import com.devs.powerfit.utils.mappers.CajaMappers.SesionCajaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SesionCajaServiceTest {

    @Mock
    private SesionCajaDao sesionCajaDao;

    @Mock
    private CajaDao cajaDao;

    @Mock
    private UsuarioDao usuarioDao;

    @Mock
    private SesionCajaMapper sesionCajaMapper;

    @InjectMocks
    private SesionCajaService sesionCajaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void create_WithNegativeInitialAmount_ShouldThrowBadRequestException() {
        SesionCajaDto sesionCajaDto = new SesionCajaDto();
        sesionCajaDto.setMontoInicial(-100.0);
        assertThrows(BadRequestException.class, () -> sesionCajaService.create(sesionCajaDto));
    }

    @Test
    public void create_WithNullDateAndTime_ShouldThrowBadRequestException() {
        SesionCajaDto sesionCajaDto = new SesionCajaDto();
        sesionCajaDto.setMontoInicial(100.0);
        assertThrows(BadRequestException.class, () -> sesionCajaService.create(sesionCajaDto));
    }

    @Test
    public void create_WithNullCajaIdAndUsuarioId_ShouldThrowBadRequestException() {
        SesionCajaDto sesionCajaDto = new SesionCajaDto();
        sesionCajaDto.setMontoInicial(100.0);
        sesionCajaDto.setFecha(new Date());
        sesionCajaDto.setHoraApertura(new Date());
        assertThrows(BadRequestException.class, () -> sesionCajaService.create(sesionCajaDto));
    }

    @Test
    public void create_WithValidData_ShouldSaveAndReturnDto() {
        // Arrange
        SesionCajaDto sesionCajaDto = new SesionCajaDto();
        sesionCajaDto.setMontoInicial(100.0);
        sesionCajaDto.setFecha(new Date());
        sesionCajaDto.setHoraApertura(new Date());
        sesionCajaDto.setIdCaja(1L);
        sesionCajaDto.setIdUsuario(1L);

        // Mocking behavior
        when(cajaDao.findByIdAndActiveTrue(anyLong())).thenReturn(Optional.of(new CajaBean()));
        when(usuarioDao.findByIdAndActiveTrue(anyLong())).thenReturn(Optional.of(new UsuarioBean()));
        when(sesionCajaMapper.toDto(any())).thenReturn(sesionCajaDto);

        // Act
        SesionCajaDto result = sesionCajaService.create(sesionCajaDto);

        // Assert
        assertNotNull(result);
        // Add more assertions as needed
    }
}
