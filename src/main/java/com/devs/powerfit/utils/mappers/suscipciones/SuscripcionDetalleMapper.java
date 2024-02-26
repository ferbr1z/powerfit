package com.devs.powerfit.utils.mappers.suscipciones;

import com.devs.powerfit.beans.suscripciones.SuscripcionDetalleBean;
import com.devs.powerfit.dtos.suscripciones.SuscripcionDetalleDto;
import com.devs.powerfit.abstracts.AbstractMapper;
import org.springframework.stereotype.Component;

@Component
public class SuscripcionDetalleMapper extends AbstractMapper<SuscripcionDetalleBean, SuscripcionDetalleDto> {
    @Override
    public SuscripcionDetalleBean toBean(SuscripcionDetalleDto dto) {
        return modelMapper.map(dto, SuscripcionDetalleBean.class);
    }

    @Override
    public SuscripcionDetalleDto toDto(SuscripcionDetalleBean bean) {
        return modelMapper.map(bean, SuscripcionDetalleDto.class);
    }
}
