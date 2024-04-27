package com.devs.powerfit.dtos.programas.clientePrograma;

import com.devs.powerfit.abstracts.AbstractDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteProgramaDto extends AbstractDto {
    private Long programaId;

    private String programa;

    @NotNull(message = "El cliente no puede ser nulo")
    private Long clienteId;

    private String nombreCliente;

    @NotNull(message = "La fecha de evaluación no puede ser nula")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaEvaluacion;

    public ClienteProgramaDto(Long id,
                              boolean active,
                              Long programaId,
                              String programa,
                              Long clienteId,
                              String nombreCliente,
                              LocalDate fechaEvaluacion) {
        super(id, active);
        this.programaId = programaId;
        this.programa = programa;
        this.clienteId = clienteId;
        this.nombreCliente = nombreCliente;
        this.fechaEvaluacion = fechaEvaluacion;
    }

}
