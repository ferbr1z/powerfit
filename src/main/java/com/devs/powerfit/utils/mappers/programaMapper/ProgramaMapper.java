package com.devs.powerfit.utils.mappers.programaMapper;

import com.devs.powerfit.abstracts.AbstractMapper;
import com.devs.powerfit.beans.programas.ProgramaBean;
import com.devs.powerfit.dtos.programas.CrearAndUpdateProgramaDto;
import com.devs.powerfit.dtos.programas.FullProgramaDto;
import com.devs.powerfit.enums.ENivelPrograma;
import com.devs.powerfit.utils.mappers.actividadMapper.ActividadMapper;
import com.devs.powerfit.utils.mappers.empleadoMappers.EmpleadoMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProgramaMapper extends AbstractMapper<ProgramaBean, CrearAndUpdateProgramaDto> {

    private ActividadMapper _actividadMapper;
    private EmpleadoMapper _empleadoMapper;

    @Autowired
    public ProgramaMapper(ActividadMapper actividadMapper, EmpleadoMapper empleadoMapper){
        _actividadMapper = actividadMapper;
        _empleadoMapper = empleadoMapper;
        configureMapper();
    }

    private void configureMapper() {
        // Mapeo de la propiedad 'actividad' de ProgramaDto a 'actividad.id' de ProgramaBean
        modelMapper.addMappings(new PropertyMap<CrearAndUpdateProgramaDto, ProgramaBean>() {
            @Override
            protected void configure() {
                map().getActividad().setId(source.getActividad());
            }
        });

        // Mapeo de la propiedad 'empleado' de ProgramaDto a 'entrenador.id' de ProgramaBean
        modelMapper.addMappings(new PropertyMap<CrearAndUpdateProgramaDto, ProgramaBean>() {
            @Override
            protected void configure() {
                map().getEmpleado().setId(source.getEmpleado());
            }
        });

    }

    @Override
    public ProgramaBean toBean(CrearAndUpdateProgramaDto dto) {
        return modelMapper.map(dto, ProgramaBean.class);
    }
    @Override
    public CrearAndUpdateProgramaDto toDto(ProgramaBean bean) {
        return modelMapper.map(bean, CrearAndUpdateProgramaDto.class);
    }

    public FullProgramaDto toFullDto(ProgramaBean bean){
        return  modelMapper.map(bean, FullProgramaDto.class);
    }

    public CrearAndUpdateProgramaDto toCreateAndUpdateDto(ProgramaBean bean){
        var newProgramaDto = new CrearAndUpdateProgramaDto();
        newProgramaDto.setActividad(bean.getActividad().getId());
        newProgramaDto.setTitulo(bean.getTitulo());
        newProgramaDto.setSexo(bean.getSexo());
        newProgramaDto.setNivel(bean.getNivel());
        newProgramaDto.setEmpleado(bean.getEmpleado().getId());
        newProgramaDto.setId(bean.getId());
        newProgramaDto.setActive(bean.isActive());
        return newProgramaDto;
    }

}
