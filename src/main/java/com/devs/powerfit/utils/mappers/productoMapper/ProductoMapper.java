package com.devs.powerfit.utils.mappers.productoMapper;

import com.devs.powerfit.abstracts.AbstractMapper;
import com.devs.powerfit.beans.productos.ProductoBean;
import com.devs.powerfit.dtos.productos.ProductoDto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper extends AbstractMapper<ProductoBean, ProductoDto> {
    @Override
    public ProductoBean toBean(ProductoDto dto) {
        return modelMapper.map(dto, ProductoBean.class);
    }
    @Override
    public ProductoDto toDto(ProductoBean bean) {
        return modelMapper.map(bean, ProductoDto.class);
    }
}
