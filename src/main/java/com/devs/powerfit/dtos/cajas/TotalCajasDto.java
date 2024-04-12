package com.devs.powerfit.dtos.cajas;

import com.devs.powerfit.abstracts.AbstractDto;
import lombok.Data;

import java.util.List;
@Data
public class TotalCajasDto extends AbstractDto {
    List<CajaDto> cajas;
    Double total;
}
