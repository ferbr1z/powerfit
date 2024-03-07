package com.devs.powerfit.utils.mappers.actividadMapper;

import com.devs.powerfit.beans.actividades.ActividadBean;
import com.devs.powerfit.dtos.actividades.ActividadDto;
import com.devs.powerfit.abstracts.AbstractMapper;
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