package com.devs.powerfit.utils.mappers.suscipcioneMapper;

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
        SuscripcionDto dto = modelMapper.map(bean, SuscripcionDto.class);
        if (bean.getActividad() != null) {
            dto.setActividadNombre(bean.getActividad().getNombre());
            dto.setCostoMensual(bean.getActividad().getCostoMensual());
            dto.setCostoSemanal(bean.getActividad().getCostoSemanal());
            dto.setDescripcion(bean.getActividad().getDescripcion());
        }
        return dto;
    }
}
