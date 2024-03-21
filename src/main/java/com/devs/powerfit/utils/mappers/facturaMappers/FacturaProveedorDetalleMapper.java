package com.devs.powerfit.utils.mappers.facturaMappers;

import com.devs.powerfit.abstracts.AbstractMapper;
import com.devs.powerfit.beans.facturas.FacturaProveedorDetalleBean;
import com.devs.powerfit.dtos.facturas.FacturaProveedorDetalleDto;
import org.springframework.stereotype.Component;

@Component
public class FacturaProveedorDetalleMapper extends AbstractMapper<FacturaProveedorDetalleBean, FacturaProveedorDetalleDto> {
    @Override
    public FacturaProveedorDetalleBean toBean(FacturaProveedorDetalleDto dto) {
        return modelMapper.map(dto, FacturaProveedorDetalleBean.class);
    }

    @Override
    public FacturaProveedorDetalleDto toDto(FacturaProveedorDetalleBean bean) {
        return modelMapper.map(bean, FacturaProveedorDetalleDto.class);
    }
}
