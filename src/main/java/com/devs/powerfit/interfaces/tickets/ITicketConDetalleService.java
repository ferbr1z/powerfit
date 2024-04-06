package com.devs.powerfit.interfaces.tickets;

import com.devs.powerfit.dtos.tickets.TicketConDetalleDto;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.utils.responses.PageResponse;

public interface ITicketConDetalleService extends IService<TicketConDetalleDto> {
    TicketConDetalleDto getByNumeroTicket(String numero);
    PageResponse<TicketConDetalleDto> searchByPagado(int page);
    PageResponse<TicketConDetalleDto> searchByPendiente(int page);
}
