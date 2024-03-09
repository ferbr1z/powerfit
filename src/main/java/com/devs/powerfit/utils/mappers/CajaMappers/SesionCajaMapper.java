package com.devs.powerfit.utils.mappers.CajaMappers;

import com.devs.powerfit.abstracts.AbstractMapper;
import com.devs.powerfit.beans.cajas.CajaBean;
import com.devs.powerfit.beans.cajas.SesionCajaBean;
import com.devs.powerfit.dtos.cajas.SesionCajaDto;
import org.springframework.stereotype.Component;

@Component
public class SesionCajaMapper extends AbstractMapper<SesionCajaBean, SesionCajaDto> {
    @Override
    public SesionCajaBean toBean(SesionCajaDto dto) {return modelMapper.map(dto, SesionCajaBean.class);
    }

    @Override
    public SesionCajaDto toDto(SesionCajaBean bean) {return modelMapper.map(bean, SesionCajaDto.class);}
}
