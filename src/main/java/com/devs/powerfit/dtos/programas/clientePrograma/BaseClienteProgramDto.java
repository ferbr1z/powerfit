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
public class BaseClienteProgramDto extends AbstractDto {
    protected Long programaId;

    @NotNull(message = "El cliente no puede ser nulo")
    protected Long clienteId;

    @NotNull(message = "La fecha de evaluación no puede ser nula")
    @JsonFormat(pattern = "yyyy-MM-dd")
    protected LocalDate fechaEvaluacion;

    protected Double porcentaje;

    public BaseClienteProgramDto(Long id, boolean active) {
        super(id, active);
    }
}
