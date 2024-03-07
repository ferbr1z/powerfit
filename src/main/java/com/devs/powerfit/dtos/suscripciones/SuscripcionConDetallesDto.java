package com.devs.powerfit.dtos.suscripciones;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuscripcionConDetallesDto extends AbstractDto {
    private SuscripcionDto suscripcionDto;
    private List<SuscripcionDetalleDto> suscripcionDetalleDtoList;
}
