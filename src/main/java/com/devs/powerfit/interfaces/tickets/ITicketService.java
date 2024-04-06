package com.devs.powerfit.interfaces.tickets;

import com.devs.powerfit.dtos.tickets.TicketDto;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.utils.responses.PageResponse;

public interface ITicketService extends IService<TicketDto> {
    TicketDto searchByNumeroTicket(String numeroTicket);
    TicketDto modificarPagado(Long id, boolean pagado);
    TicketDto actualizarSaldo(Long id, double nuevoSaldo);
    PageResponse<TicketDto> searchByPendiente(int page);
    PageResponse<TicketDto> searchByPagado(int page);
}
