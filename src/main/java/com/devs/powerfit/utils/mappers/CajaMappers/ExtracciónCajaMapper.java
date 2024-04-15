package com.devs.powerfit.utils.mappers.CajaMappers;

import com.devs.powerfit.abstracts.AbstractMapper;
import com.devs.powerfit.beans.cajas.ExtraccionDeCajaBean;
import com.devs.powerfit.dtos.cajas.ExtraccionDeCajaDto;
import org.springframework.stereotype.Component;

@Component
public class ExtracciónCajaMapper extends AbstractMapper<ExtraccionDeCajaBean, ExtraccionDeCajaDto> {
    @Override
    public ExtraccionDeCajaBean toBean(ExtraccionDeCajaDto dto) {
        return modelMapper.map(dto,ExtraccionDeCajaBean.class);
    }

    @Override
    public ExtraccionDeCajaDto toDto(ExtraccionDeCajaBean bean) {
        return modelMapper.map(bean, ExtraccionDeCajaDto.class);
    }
}
