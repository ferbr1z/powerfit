package com.devs.powerfit.dtos.programas.clientePrograma;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteProgramaDto extends BaseClienteProgramDto {

    private String nombreCliente;
    private String programa;

    private List<ClienteProgramaItemDto> clienteProgramaItem;


    public ClienteProgramaDto(Long id,
                              boolean active,
                              Long programaId,
                              String programa,
                              Long clienteId,
                              String nombreCliente,
                              LocalDate fechaEvaluacion,
                              Double porcentaje) {
        super(id, active);
        this.programaId = programaId;
        this.programa = programa;
        this.clienteId = clienteId;
        this.nombreCliente = nombreCliente;
        this.fechaEvaluacion = fechaEvaluacion;
        this.porcentaje = porcentaje;
    }

}
