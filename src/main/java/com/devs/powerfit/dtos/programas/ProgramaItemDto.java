package com.devs.powerfit.dtos.programas;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramaItemDto extends AbstractDto {
    private Long programaId;
    private String nombre;
    private String descripcion;
    private String tiempo;
    private Double peso;
    private Integer repeticiones;
}
