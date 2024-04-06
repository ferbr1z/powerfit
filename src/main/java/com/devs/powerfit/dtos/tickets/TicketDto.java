package com.devs.powerfit.dtos.tickets;

import com.devs.powerfit.abstracts.AbstractDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class TicketDto extends AbstractDto {
    private Long sesionId;
    private String timbrado;
    private String nroTicket;
    private String nombreCaja;
    private String nombreEmpleado;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fecha;
    private Double total;
    private Double subTotal;
    private Double saldo;
    private Double iva5;
    private Double iva10;
    private Double ivaTotal;
    private boolean pagado;
}
