package com.devs.powerfit.utils.mappers.proveedorMapper;

import com.devs.powerfit.abstracts.AbstractMapper;
import com.devs.powerfit.beans.proveedores.ProveedorBean;
import com.devs.powerfit.dtos.proveedores.ProveedorDto;
import org.springframework.stereotype.Component;

@Component
public class ProveedorMapper extends AbstractMapper<ProveedorBean, ProveedorDto> {
    @Override
    public ProveedorBean toBean(ProveedorDto dto) {
        return modelMapper.map(dto, ProveedorBean.class);
    }

    @Override
    public ProveedorDto toDto(ProveedorBean bean) {
        return modelMapper.map(bean, ProveedorDto.class);
    }
}
