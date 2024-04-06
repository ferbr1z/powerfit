package com.devs.powerfit.dtos.tickets;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.Data;

import java.util.List;

@Data
public class TicketConDetalleDto extends AbstractDto {
    private TicketDto ticket;
    private List<TicketDetalleDto> detalles;
}
