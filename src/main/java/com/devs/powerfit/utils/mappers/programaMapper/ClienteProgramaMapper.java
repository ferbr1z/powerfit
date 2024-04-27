package com.devs.powerfit.utils.mappers.programaMapper;

import com.devs.powerfit.abstracts.AbstractMapper;
import com.devs.powerfit.beans.programas.ClienteProgramaBean;
import com.devs.powerfit.dtos.programas.clientePrograma.ClienteProgramaDto;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class ClienteProgramaMapper extends AbstractMapper<ClienteProgramaBean, ClienteProgramaDto> {

    public ClienteProgramaMapper() {
        configureMapper();
    }

    private void configureMapper() {
        modelMapper.addMappings(new PropertyMap<ClienteProgramaBean, ClienteProgramaDto>() {

            @Override
            protected void configure() {
                map().setProgramaId(source.getPrograma().getId());
                map().setPrograma(source.getPrograma().getTitulo());
                map().setClienteId(source.getCliente().getId());
                map().setNombreCliente(source.getCliente().getNombre());
            }
        });

        modelMapper.addMappings(new PropertyMap<ClienteProgramaDto, ClienteProgramaBean>() {

            @Override
            protected void configure() {
                map().getPrograma().setId(source.getProgramaId());
                map().getCliente().setId(source.getClienteId());
            }
        });

    }

    @Override
    public ClienteProgramaBean toBean(ClienteProgramaDto dto) {
        return modelMapper.map(dto, ClienteProgramaBean.class);
    }

    @Override
    public ClienteProgramaDto toDto(ClienteProgramaBean bean) {
        return modelMapper.map(bean, ClienteProgramaDto.class);
    }
}
