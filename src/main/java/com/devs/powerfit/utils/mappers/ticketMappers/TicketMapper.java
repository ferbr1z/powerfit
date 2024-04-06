package com.devs.powerfit.utils.mappers.ticketMappers;

import com.devs.powerfit.abstracts.AbstractMapper;
import com.devs.powerfit.beans.tickets.TicketBean;
import com.devs.powerfit.dtos.tickets.TicketDto;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper extends AbstractMapper<TicketBean, TicketDto> {
    @Override
    public TicketBean toBean(TicketDto dto) {return modelMapper.map(dto,TicketBean.class);}

    @Override
    public TicketDto toDto(TicketBean bean) {return modelMapper.map(bean,TicketDto.class);}
}
