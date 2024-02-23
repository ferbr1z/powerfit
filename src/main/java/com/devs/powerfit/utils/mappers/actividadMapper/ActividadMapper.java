package com.devs.powerfit.utils.mappers.actividadMapper;

import com.devs.powerfit.beans.ActividadBean;
import com.devs.powerfit.beans.ActividadBean;
import com.devs.powerfit.dtos.ActividadDto;
import com.devs.powerfit.dtos.ActividadDto;
import com.devs.powerfit.utils.mappers.AbstractMapper;
import org.springframework.stereotype.Component;

@Component
public class ActividadMapper extends AbstractMapper<ActividadBean, ActividadDto> {
    @Override
    public ActividadBean toBean(ActividadDto dto) {
        return modelMapper.map(dto, ActividadBean.class);
    }

    @Override
    public ActividadDto toDto(ActividadBean bean) {
        return modelMapper.map(bean, ActividadDto.class);
    }

}