package com.devs.powerfit.utils.mappers.ticketMappers;

import com.devs.powerfit.abstracts.AbstractMapper;
import com.devs.powerfit.beans.tickets.TicketDetalleBean;
import com.devs.powerfit.dtos.tickets.TicketDetalleDto;
import org.springframework.stereotype.Component;

@Component
public class TicketDetalleMapper extends AbstractMapper<TicketDetalleBean, TicketDetalleDto> {
    @Override
    public TicketDetalleBean toBean(TicketDetalleDto dto) {
        return modelMapper.map(dto, TicketDetalleBean.class);
    }

    @Override
    public TicketDetalleDto toDto(TicketDetalleBean bean) {
        TicketDetalleDto dto = modelMapper.map(bean, TicketDetalleDto.class);
        if (bean.getProducto() != null) {
            dto.setProductoNombre(bean.getProducto().getNombre());
        }
        return dto;
    }
}
