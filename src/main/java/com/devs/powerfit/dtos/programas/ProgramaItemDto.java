package com.devs.powerfit.dtos.programas;

import com.devs.powerfit.abstracts.AbstractDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramaItemDto extends AbstractDto {
    @NotNull(message = "El id del programa es requerido")
    private Long programaId;
    @NotNull(message = "El nombre es requerido")
    private String nombre;
    private String descripcion;
    private String tiempo;
    private Double peso;
    private Integer repeticiones;
}
