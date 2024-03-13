package com.devs.powerfit.utils.mappers.facturaMappers;

import com.devs.powerfit.abstracts.AbstractMapper;
import com.devs.powerfit.beans.facturas.FacturaBean;
import com.devs.powerfit.dtos.facturas.FacturaDto;
import org.springframework.stereotype.Component;

@Component
public class FacturaMapper extends AbstractMapper<FacturaBean, FacturaDto> {
    @Override
    public FacturaBean toBean(FacturaDto dto) {
        return modelMapper.map(dto, FacturaBean.class);
    }

    @Override
    public FacturaDto toDto(FacturaBean bean) {
        return modelMapper.map(bean, FacturaDto.class);
    }
}
