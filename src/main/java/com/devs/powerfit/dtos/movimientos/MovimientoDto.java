package com.devs.powerfit.dtos.movimientos;

import com.devs.powerfit.abstracts.AbstractDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class MovimientoDto extends AbstractDto {
    private Long facturaId;
    private Long facturaProveedorId;
    private Long sesionId;
    private Long ticketId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime hora;
    private Double total;
    private boolean entrada;
    private String nombreCaja;
    private String nombreEmpleado;
}
