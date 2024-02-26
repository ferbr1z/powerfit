package com.devs.powerfit.utils.mappers.clienteMappers;

import com.devs.powerfit.beans.clientes.ClienteBean;
import com.devs.powerfit.dtos.clientes.ClienteDto;
import com.devs.powerfit.abstracts.AbstractMapper;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper extends AbstractMapper<ClienteBean, ClienteDto> {
    @Override
    public ClienteBean toBean(ClienteDto dto) {
        return modelMapper.map(dto, ClienteBean.class);
    }

    @Override
    public ClienteDto toDto(ClienteBean bean) {
        return modelMapper.map(bean, ClienteDto.class);
    }

}