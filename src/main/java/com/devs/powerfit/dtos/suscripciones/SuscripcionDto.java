package com.devs.powerfit.dtos.suscripciones;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuscripcionDto extends AbstractDto {
    private Long clienteID;
    private Double total;

}
