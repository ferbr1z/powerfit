package com.devs.powerfit.utils.mappers.programaMapper;

import com.devs.powerfit.abstracts.AbstractMapper;
import com.devs.powerfit.beans.programas.ClienteProgramaItem;
import com.devs.powerfit.dtos.programas.clientePrograma.ClienteProgramaItemDto;
import org.springframework.stereotype.Component;

@Component
public class ClienteProgramaItemMapper extends AbstractMapper<ClienteProgramaItem, ClienteProgramaItemDto> {
    @Override
    public ClienteProgramaItem toBean(ClienteProgramaItemDto dto) {
        return modelMapper.map(dto, ClienteProgramaItem.class);
    }

    @Override
    public ClienteProgramaItemDto toDto(ClienteProgramaItem bean) {
        return modelMapper.map(bean, ClienteProgramaItemDto.class);
    }
}
