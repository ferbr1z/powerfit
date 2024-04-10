package com.devs.powerfit.interfaces.tickets;

import com.devs.powerfit.dtos.tickets.TicketDetalleDto;
import com.devs.powerfit.interfaces.bases.IService;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ITicketDetalleService extends IService<TicketDetalleDto> {
    List<TicketDetalleDto> getAllByTicketId(Long ticketId);

    List<TicketDetalleDto> getAllDetalles();

    List<TicketDetalleDto> getAllDetallesBetween(LocalDate fechaInicio, LocalDate fechaFin);
}
