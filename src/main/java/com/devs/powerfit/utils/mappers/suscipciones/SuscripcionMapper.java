package com.devs.powerfit.utils.mappers.suscipciones;

import com.devs.powerfit.beans.suscripciones.SuscripcionBean;
import com.devs.powerfit.dtos.suscripciones.SuscripcionDto;
import com.devs.powerfit.abstracts.AbstractMapper;
import org.springframework.stereotype.Component;

@Component
public class SuscripcionMapper extends AbstractMapper<SuscripcionBean, SuscripcionDto> {
    @Override
    public SuscripcionBean toBean(SuscripcionDto dto) {
        return modelMapper.map(dto, SuscripcionBean.class);
    }

    @Override
    public SuscripcionDto toDto(SuscripcionBean bean) {
        return modelMapper.map(bean, SuscripcionDto.class);
    }
}
