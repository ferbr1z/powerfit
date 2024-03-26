package com.devs.powerfit.utils.mappers.arqueoMapper;

import com.devs.powerfit.abstracts.AbstractMapper;
import com.devs.powerfit.beans.arqueo.ArqueoDetalleBean;
import com.devs.powerfit.dtos.arqueo.ArqueoDetalleDto;
import org.springframework.stereotype.Component;

@Component
public class ArqueoDetalleMapper extends AbstractMapper<ArqueoDetalleBean, ArqueoDetalleDto> {
    @Override
    public ArqueoDetalleBean toBean(ArqueoDetalleDto dto) {
        return modelMapper.map(dto, ArqueoDetalleBean.class);
    }

    @Override
    public ArqueoDetalleDto toDto(ArqueoDetalleBean bean) {
        return modelMapper.map(bean, ArqueoDetalleDto.class);
    }
}
