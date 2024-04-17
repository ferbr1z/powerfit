package com.devs.powerfit.services.clientes;

import com.devs.powerfit.beans.clientes.ClienteBean;
import com.devs.powerfit.daos.clientes.ClienteDao;
import com.devs.powerfit.dtos.clientes.ClienteDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.utils.mappers.clienteMappers.ClienteMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ClienteServiceTest {
    @Mock
    private ClienteDao clienteDao;
    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteService clienteService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testCreateExistingActiveClienteByCedula() {
        // Datos de prueba
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("John Doe");
        clienteDto.setCedula("123456789");
        clienteDto.setEmail("johndoe@example.com");

        // Mocking del clienteDao
        ClienteBean existingCliente = new ClienteBean();
        existingCliente.setActive(true);
        Mockito.when(clienteDao.findByCedula(clienteDto.getCedula())).thenReturn(Optional.of(existingCliente));
        Mockito.when(clienteDao.findByEmail(clienteDto.getEmail())).thenReturn(Optional.empty());

        // Llamada al método create y verificación de la excepción lanzada
        Assertions.assertThrows(BadRequestException.class, () -> clienteService.create(clienteDto));
        Mockito.verify(clienteDao, Mockito.never()).save(any(ClienteBean.class));
    }

    @Test
    public void testCreateExistingActiveClienteByEmail() {
        // Datos de prueba
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("John Doe");
        clienteDto.setCedula("123456789");
        clienteDto.setEmail("johndoe@example.com");

        // Mocking del clienteDao
        ClienteBean existingCliente = new ClienteBean();
        existingCliente.setActive(true);
        Mockito.when(clienteDao.findByCedula(clienteDto.getCedula())).thenReturn(Optional.empty());
        Mockito.when(clienteDao.findByEmail(clienteDto.getEmail())).thenReturn(Optional.of(existingCliente));

        // Llamada al método create y verificación de la excepción lanzada
        Assertions.assertThrows(BadRequestException.class, () -> clienteService.create(clienteDto));
        Mockito.verify(clienteDao, Mockito.never()).save(any(ClienteBean.class));
    }
    @Test
    void create_ClienteWithExistingCedula_Error() {
        // Arrange
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setCedula("1234567890");


        when(clienteDao.findByCedula("1234567890")).thenReturn(Optional.of(new ClienteBean()));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> clienteService.create(clienteDto));
    }
    @Test
    void create_ClienteWithNoName_Error() {
        // Arrange
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setCedula("098526377");


        // Act & Assert
        assertThrows(BadRequestException.class, () -> clienteService.create(clienteDto));
    }
    @Test
    void create_ClienteWithNoCedula_Error() {
        // Arrange
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Marcelo");
        // Act & Assert
        assertThrows(BadRequestException.class, () -> clienteService.create(clienteDto));
    }
    @Test
    void getById_NonExistingClient_ThrowsNotFoundException() {
        // Arrange
        Long id = 1L;
        when(clienteDao.findByIdAndActiveTrue(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> clienteService.getById(id));
    }
    @Test
    void getById_ExistingClient_ReturnsClientDto() {
        Long id = 1L;
        ClienteDto dtoExistente=new ClienteDto();
        dtoExistente.setId(id);
        dtoExistente.setNombre("John Doe");
        dtoExistente.setActive(true);
        ClienteBean clienteExistente = new ClienteBean(); // Crea un cliente existente
        clienteExistente.setId(id);
        clienteExistente.setNombre("John Doe");
        clienteExistente.setActive(true);
        when((clienteMapper.toDto(clienteExistente))).thenReturn(dtoExistente);

        when(clienteDao.findByIdAndActiveTrue(id)).thenReturn(Optional.of(clienteExistente)); // Configura el mock para que devuelva el cliente existente

        // Act
        ClienteDto clienteDto = clienteService.getById(id);

        // Assert
        assertEquals(clienteExistente.getNombre(), clienteDto.getNombre());
        assertEquals(clienteExistente.getId(), clienteDto.getId());
        // Asegúrate de verificar otros atributos que esperas en el DTO
    }
    @Test
    void update_NonExistingClient_ThrowsNotFoundException() {
        // Arrange
        Long id = 1L;
        ClienteDto clienteDto = new ClienteDto();

        when(clienteDao.findByIdAndActiveTrue(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> clienteService.update(id, clienteDto));
    }
    @Test
    void delete_NonExistingClient_ThrowsNotFoundException() {
        // Arrange
        Long id = 1L;
        ClienteDto clienteDto = new ClienteDto();

        when(clienteDao.findByIdAndActiveTrue(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> clienteService.delete(id));
    }
    @Test
    void delete_ExistingClient_ThrowsNotFoundException() {
        // Arrange
        Long id = 1L;
        ClienteBean eliminado=new ClienteBean();
        eliminado.setActive(true);

        when(clienteDao.findByIdAndActiveTrue(id)).thenReturn(Optional.of(eliminado));
        boolean result=clienteService.delete(id);
        assertTrue(result);
    }





}