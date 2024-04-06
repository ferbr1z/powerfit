package com.devs.powerfit.services.tickets;

import com.devs.powerfit.dtos.tickets.TicketConDetalleDto;
import com.devs.powerfit.dtos.tickets.TicketDetalleDto;
import com.devs.powerfit.dtos.tickets.TicketDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.tickets.ITicketConDetalleService;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TicketConDetalleService implements ITicketConDetalleService {
    private final TicketService ticketService;
    private final TicketDetalleService ticketDetalleService;
    @Autowired
    public TicketConDetalleService(TicketService ticketService, TicketDetalleService ticketDetalleService) {
        this.ticketService = ticketService;
        this.ticketDetalleService = ticketDetalleService;
    }

    @Override
    public TicketConDetalleDto create(TicketConDetalleDto ticketConDetalleDto) {
        // Verificar si la lista de detalles está vacía
        if (ticketConDetalleDto.getDetalles() == null || ticketConDetalleDto.getDetalles().isEmpty()) {
            // Puedes manejar esta situación de acuerdo a tus requerimientos, como lanzar una excepción, enviar un mensaje de error, etc.
            // Por ejemplo, lanzar una excepción:
            throw new BadRequestException("La lista de detalles del ticket está vacía.");
        }

        // Crear el ticket principal
        TicketDto ticketCreado = ticketService.create(ticketConDetalleDto.getTicket());

        // Crear los detalles del ticket
        List<TicketDetalleDto> detallesCreados = ticketConDetalleDto.getDetalles().stream()
                .map(detalle -> {
                    detalle.setTicketId(ticketCreado.getId()); // Asignar el ID del ticket principal al detalle
                    return ticketDetalleService.create(detalle);
                })
                .collect(Collectors.toList());

        // Crear y devolver el ticket con los detalles creados
        TicketConDetalleDto nuevo = new TicketConDetalleDto();
        nuevo.setTicket(ticketCreado);
        nuevo.setDetalles(detallesCreados);
        return nuevo;
    }

    @Override
    public TicketConDetalleDto getById(Long id) {
        // Obtener el ticket principal por su ID
        TicketDto ticketDto = ticketService.getById(id);
        // Obtener los detalles del ticket por el ID del ticket principal
        List<TicketDetalleDto> detallesDto = ticketDetalleService.getAllByTicketId(id);
        // Crear y devolver el objeto TicketConDetalleDto
        TicketConDetalleDto nuevo = new TicketConDetalleDto();
        nuevo.setTicket(ticketDto);
        nuevo.setDetalles(detallesDto);
        return nuevo;
    }

    @Override
    public PageResponse<TicketConDetalleDto> getAll(int page) {
        // Obtener todos los tickets principales
        PageResponse<TicketDto> ticketPage = ticketService.getAll(page);

        // Para cada ticket, obtener sus detalles
        List<TicketConDetalleDto> ticketConDetalleList = ticketPage.getItems().stream()
                .map(ticketDto -> getById(ticketDto.getId())) // Obtener el ticket con detalles
                .collect(Collectors.toList());

        // Devolver la lista de tickets con detalles
        return new PageResponse<>(ticketConDetalleList, ticketPage.getTotalPages(), ticketPage.getTotalItems(), ticketPage.getCurrentPage());
    }


    @Override
    public TicketConDetalleDto update(Long id, TicketConDetalleDto ticketConDetalleDto) {
        throw new BadRequestException("No es posible actualizar un ticket");
    }

    @Override
    public boolean delete(Long id) {return false;}

    @Override
    public TicketConDetalleDto getByNumeroTicket(String numero) {
        // Obtener el ticket principal por su número
        TicketDto ticketDto = ticketService.searchByNumeroTicket(numero);
        if (ticketDto == null) {
            // Manejar el caso en el que no se encuentre el ticket
            throw new NotFoundException("No se encontró un ticket con el número proporcionado: " + numero);
        }
        // Obtener los detalles del ticket por el ID del ticket principal
        List<TicketDetalleDto> detallesDto = ticketDetalleService.getAllByTicketId(ticketDto.getId());
        // Crear y devolver el objeto TicketConDetalleDto
        TicketConDetalleDto nuevo = new TicketConDetalleDto();
        nuevo.setTicket(ticketDto);
        nuevo.setDetalles(detallesDto);
        return nuevo;
    }

    @Override
    public PageResponse<TicketConDetalleDto> searchByPagado(int page) {
        // Obtener todos los tickets pagados
        PageResponse<TicketDto> ticketPage = ticketService.searchByPagado(page);

        // Para cada ticket, obtener sus detalles
        List<TicketConDetalleDto> ticketConDetalleList = ticketPage.getItems().stream()
                .map(ticketDto -> getById(ticketDto.getId())) // Obtener el ticket con detalles
                .collect(Collectors.toList());

        // Devolver la lista de tickets con detalles pagados
        return new PageResponse<>(ticketConDetalleList, ticketPage.getTotalPages(), ticketPage.getTotalItems(), ticketPage.getCurrentPage());
    }

    @Override
    public PageResponse<TicketConDetalleDto> searchByPendiente(int page) {
        // Obtener todos los tickets pendientes
        PageResponse<TicketDto> ticketPage = ticketService.searchByPendiente(page);

        // Para cada ticket, obtener sus detalles
        List<TicketConDetalleDto> ticketConDetalleList = ticketPage.getItems().stream()
                .map(ticketDto -> getById(ticketDto.getId())) // Obtener el ticket con detalles
                .collect(Collectors.toList());

        // Devolver la lista de tickets con detalles pendientes
        return new PageResponse<>(ticketConDetalleList, ticketPage.getTotalPages(), ticketPage.getTotalItems(), ticketPage.getCurrentPage());
    }
}
