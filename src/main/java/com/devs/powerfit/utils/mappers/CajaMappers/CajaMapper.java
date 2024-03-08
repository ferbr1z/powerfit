package com.devs.powerfit.utils.mappers.CajaMappers;

import com.devs.powerfit.abstracts.AbstractMapper;
import com.devs.powerfit.beans.cajas.CajaBean;
import com.devs.powerfit.dtos.cajas.CajaDto;

public class CajaMapper extends AbstractMapper<CajaBean, CajaDto> {
    @Override
    public CajaBean toBean(CajaDto dto) {
        return modelMapper.map(dto, CajaBean.class);
    }

    @Override
    public CajaDto toDto(CajaBean bean) {
        return modelMapper.map(bean, CajaDto.class);
    }
}

