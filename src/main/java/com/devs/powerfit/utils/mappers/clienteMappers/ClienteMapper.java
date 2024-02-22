package com.devs.powerfit.utils.mappers.clienteMappers;

import com.devs.powerfit.beans.ClienteBean;
import com.devs.powerfit.dtos.ClienteDto;
import com.devs.powerfit.utils.mappers.AbstractMapper;

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