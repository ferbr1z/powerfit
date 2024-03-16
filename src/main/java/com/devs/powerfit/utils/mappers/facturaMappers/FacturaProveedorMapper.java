package com.devs.powerfit.utils.mappers.facturaMappers;

import com.devs.powerfit.abstracts.AbstractMapper;
import com.devs.powerfit.beans.facturas.FacturaProveedorBean;
import com.devs.powerfit.dtos.facturas.FacturaProveedorDto;
import org.springframework.stereotype.Component;

@Component
public class FacturaProveedorMapper extends AbstractMapper<FacturaProveedorBean, FacturaProveedorDto> {
    @Override
    public FacturaProveedorBean toBean(FacturaProveedorDto dto) {
        return modelMapper.map(dto, FacturaProveedorBean.class);
    }

    @Override
    public FacturaProveedorDto toDto(FacturaProveedorBean bean) {
        return modelMapper.map(bean, FacturaProveedorDto.class);
    }
}
