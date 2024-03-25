package com.devs.powerfit.utils.mappers.tiposDePagoMapper;

import com.devs.powerfit.abstracts.AbstractMapper;
import com.devs.powerfit.beans.proveedores.ProveedorBean;
import com.devs.powerfit.beans.tipoDePagos.TipoDePagoBean;
import com.devs.powerfit.dtos.tiposDePagos.TipoDePagoDto;
import org.springframework.stereotype.Component;

@Component
public class TipoDePagoMapper extends AbstractMapper<TipoDePagoBean, TipoDePagoDto> {
    @Override
    public TipoDePagoBean toBean(TipoDePagoDto dto) {
        return modelMapper.map(dto, TipoDePagoBean.class);
    }

    @Override
    public TipoDePagoDto toDto(TipoDePagoBean bean) {
        return modelMapper.map(bean, TipoDePagoDto.class);
    }
}
