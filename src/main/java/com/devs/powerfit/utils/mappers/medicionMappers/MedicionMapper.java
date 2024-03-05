package com.devs.powerfit.utils.mappers.medicionMappers;

import com.devs.powerfit.abstracts.AbstractMapper;
import com.devs.powerfit.beans.mediciones.MedicionBean;
import com.devs.powerfit.dtos.clientes.ClienteDto;
import com.devs.powerfit.dtos.mediciones.MedicionDto;
import org.springframework.stereotype.Component;

@Component
public class MedicionMapper extends AbstractMapper<MedicionBean, MedicionDto> {
    @Override
    public MedicionBean toBean(MedicionDto dto) {
        return modelMapper.map(dto, MedicionBean.class);
    }

    @Override
    public MedicionDto toDto(MedicionBean bean) {
        return modelMapper.map(bean, MedicionDto.class);
    }
}
