package com.devs.powerfit.utils.mappers.empleadoMappers;

import com.devs.powerfit.abstracts.AbstractMapper;
import com.devs.powerfit.beans.empleados.EmpleadoBean;
import com.devs.powerfit.dtos.empleados.EmpleadoDto;
import org.springframework.stereotype.Component;

@Component
public class EmpleadoMapper extends AbstractMapper<EmpleadoBean, EmpleadoDto> {
    @Override
    public EmpleadoBean toBean(EmpleadoDto dto) {
        return modelMapper.map(dto, EmpleadoBean.class);
    }

    @Override
    public EmpleadoDto toDto(EmpleadoBean bean) {
        return modelMapper.map(bean, EmpleadoDto.class);
    }
}
