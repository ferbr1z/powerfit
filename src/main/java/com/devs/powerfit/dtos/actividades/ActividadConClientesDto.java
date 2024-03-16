package com.devs.powerfit.dtos.actividades;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActividadConClientesDto extends AbstractDto {
    private ActividadDto actividad;
    private Long clientes;
}
