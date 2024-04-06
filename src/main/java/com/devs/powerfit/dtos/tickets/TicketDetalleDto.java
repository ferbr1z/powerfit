package com.devs.powerfit.dtos.tickets;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.Data;

@Data
public class TicketDetalleDto extends AbstractDto {
    private Long ticketId;
    private Long productoId;
    private String productoNombre;
    private Double precioUnitario;
    private Integer cantidad;
    private Double subtotal;
    private Double iva;
    private Double ivaTotal;
}
