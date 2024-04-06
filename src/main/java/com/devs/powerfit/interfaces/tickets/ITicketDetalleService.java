package com.devs.powerfit.interfaces.tickets;

import com.devs.powerfit.dtos.tickets.TicketDetalleDto;
import com.devs.powerfit.interfaces.bases.IService;

import java.util.List;

public interface ITicketDetalleService extends IService<TicketDetalleDto> {
    List<TicketDetalleDto> getAllByTicketId(Long ticketId);
}
