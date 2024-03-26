package com.devs.powerfit.utils.mappers.arqueoMapper;

import com.devs.powerfit.abstracts.AbstractMapper;
import com.devs.powerfit.beans.arqueo.ArqueoBean;
import com.devs.powerfit.dtos.arqueo.ArqueoDto;
import org.springframework.stereotype.Component;

@Component
public class ArqueoMapper extends AbstractMapper<ArqueoBean, ArqueoDto> {
    @Override
    public ArqueoBean toBean(ArqueoDto dto) {
        return modelMapper.map(dto, ArqueoBean.class);
    }

    @Override
    public ArqueoDto toDto(ArqueoBean bean) {
        return modelMapper.map(bean, ArqueoDto.class);
    }
}
