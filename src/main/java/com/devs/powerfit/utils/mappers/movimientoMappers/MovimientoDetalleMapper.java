package com.devs.powerfit.utils.mappers.movimientoMappers;

import com.devs.powerfit.abstracts.AbstractMapper;
import com.devs.powerfit.beans.movimientos.MovimientoDetalleBean;
import com.devs.powerfit.dtos.movimientos.MovimientoDetalleDto;
import org.springframework.stereotype.Component;

@Component
public class MovimientoDetalleMapper extends AbstractMapper<MovimientoDetalleBean, MovimientoDetalleDto> {
    @Override
    public MovimientoDetalleBean toBean(MovimientoDetalleDto dto) {return modelMapper.map(dto, MovimientoDetalleBean.class);}

    @Override
    public MovimientoDetalleDto toDto(MovimientoDetalleBean bean) {return modelMapper.map(bean, MovimientoDetalleDto.class);}
}
