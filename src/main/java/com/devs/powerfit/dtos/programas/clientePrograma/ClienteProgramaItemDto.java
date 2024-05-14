package com.devs.powerfit.dtos.programas.clientePrograma;

import com.devs.powerfit.abstracts.AbstractDto;
import com.devs.powerfit.dtos.programas.ProgramaItemDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClienteProgramaItemDto extends AbstractDto {
    private Long clienteProgramaId;
    @NotNull(message = "Programa item no puede ser nulo")
    private ProgramaItemDto programaItem;
    @NotNull(message = "El campo logrado no puede ser nulo")
    private Boolean logrado;
}
