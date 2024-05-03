package com.devs.powerfit.utils.mappers.programaMapper;

import com.devs.powerfit.abstracts.AbstractMapper;
import com.devs.powerfit.beans.programas.ProgramaItemBean;
import com.devs.powerfit.dtos.programas.ProgramaItemDto;
import org.springframework.stereotype.Component;

@Component
public class ProgramaItemMapper extends AbstractMapper<ProgramaItemBean, ProgramaItemDto> {
    @Override
    public ProgramaItemBean toBean(ProgramaItemDto dto) {
        return modelMapper.map(dto, ProgramaItemBean.class);
    }

    @Override
    public ProgramaItemDto toDto(ProgramaItemBean bean) {
        return modelMapper.map(bean, ProgramaItemDto.class);
    }
}
