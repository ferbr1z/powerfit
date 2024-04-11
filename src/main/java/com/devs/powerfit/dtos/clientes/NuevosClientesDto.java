package com.devs.powerfit.dtos.clientes;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.Data;

@Data
public class NuevosClientesDto extends AbstractDto {
    private Long cantidadNuevosClientes;
}
