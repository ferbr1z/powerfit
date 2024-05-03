package com.devs.powerfit.dtos.programas.clientePrograma;

import com.devs.powerfit.abstracts.AbstractDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClienteProgramaItemDto extends AbstractDto {
    private Long clienteProgramaId;
    @NotNull(message = "Programa item no puede ser nulo")
    private Long programaItemId;
    @NotNull(message = "El tiempo no puede ser nulo")
    private String tiempo;
    @NotNull(message = "Las repeticiones no pueden ser nulas")
    private Integer repeticiones;
    @NotNull(message = "El campo peso no puede ser nulo")
    private Double peso;
    @NotNull(message = "El campo logrado no puede ser nulo")
    private Boolean logrado;
}
