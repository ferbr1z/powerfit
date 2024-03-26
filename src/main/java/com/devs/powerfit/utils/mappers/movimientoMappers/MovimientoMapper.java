package com.devs.powerfit.utils.mappers.movimientoMappers;

import com.devs.powerfit.abstracts.AbstractMapper;
import com.devs.powerfit.beans.movimientos.MovimientoBean;
import com.devs.powerfit.dtos.movimientos.MovimientoDto;
import org.springframework.stereotype.Component;

@Component
public class MovimientoMapper extends AbstractMapper<MovimientoBean, MovimientoDto> {
    @Override
    public MovimientoBean toBean(MovimientoDto dto) {return modelMapper.map(dto,MovimientoBean.class);}

    @Override
    public MovimientoDto toDto(MovimientoBean bean) {return modelMapper.map(bean,MovimientoDto.class);}
}
