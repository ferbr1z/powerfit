package com.devs.powerfit.dtos.suscripciones;

import com.devs.powerfit.abstracts.AbstractDto;
import com.devs.powerfit.dtos.clientes.ClienteDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuscripcionConClienteDto extends AbstractDto {
    private SuscripcionDto suscripcionDto;
    private ClienteDto clienteDto;
}
