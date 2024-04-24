package com.devs.powerfit.utils.mappers.facturaMappers;

import com.devs.powerfit.abstracts.AbstractMapper;
import com.devs.powerfit.beans.facturas.FacturaBean;
import com.devs.powerfit.beans.facturas.FacturaDetalleBean;
import com.devs.powerfit.dtos.facturas.FacturaDetalleDto;
import com.devs.powerfit.dtos.facturas.FacturaDto;
import org.springframework.stereotype.Component;

@Component
public class FacturaDetalleMapper extends AbstractMapper<FacturaDetalleBean, FacturaDetalleDto> {
    @Override
    public FacturaDetalleBean toBean(FacturaDetalleDto dto) {
        return modelMapper.map(dto, FacturaDetalleBean.class);
    }

    @Override
    public FacturaDetalleDto toDto(FacturaDetalleBean bean) {
        FacturaDetalleDto dto = modelMapper.map(bean, FacturaDetalleDto.class);
        if (bean.getProducto() != null) {
            dto.setProductoNombre(bean.getProducto().getNombre());
        }
        return dto;
    }
}