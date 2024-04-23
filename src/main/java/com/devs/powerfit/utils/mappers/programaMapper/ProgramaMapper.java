package com.devs.powerfit.utils.mappers.programaMapper;

import com.devs.powerfit.abstracts.AbstractMapper;
import com.devs.powerfit.beans.programas.ProgramaBean;
import com.devs.powerfit.dtos.programas.ProgramaDto;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class ProgramaMapper extends AbstractMapper<ProgramaBean, ProgramaDto> {

    public ProgramaMapper(){
        configureMapper();
    }

    private void configureMapper() {
        // Mapeo de la propiedad 'actividad' de ProgramaDto a 'actividad.id' de ProgramaBean
        modelMapper.addMappings(new PropertyMap<ProgramaDto, ProgramaBean>() {
            @Override
            protected void configure() {
                map().getActividad().setId(source.getActividad());
            }
        });

        // Mapeo de la propiedad 'empleado' de ProgramaDto a 'entrenador.id' de ProgramaBean
        modelMapper.addMappings(new PropertyMap<ProgramaDto, ProgramaBean>() {
            @Override
            protected void configure() {
                map().getEmpleado().setId(source.getEmpleado());
            }
        });
    }

    @Override
    public ProgramaBean toBean(ProgramaDto dto) {
        return modelMapper.map(dto, ProgramaBean.class);
    }
    @Override
    public ProgramaDto toDto(ProgramaBean bean) {
        return modelMapper.map(bean, ProgramaDto.class);
    }

    public ProgramaDto toCreateDto(ProgramaBean bean){
        var newProgramaDto = new ProgramaDto();
        newProgramaDto.setActividad(bean.getActividad().getId());
        newProgramaDto.setTitulo(bean.getTitulo());
        newProgramaDto.setSexo(bean.getSexo());
        newProgramaDto.setNivel(bean.getNivel());
        newProgramaDto.setEmpleado(bean.getEmpleado().getId());
        newProgramaDto.setId(bean.getId());
        return newProgramaDto;
    }

}
