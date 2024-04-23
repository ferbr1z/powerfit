package com.devs.powerfit.services.programas;

import com.devs.powerfit.beans.programas.ProgramaBean;
import com.devs.powerfit.daos.programas.ProgramaDao;
import com.devs.powerfit.dtos.programas.CrearAndUpdateProgramaDto;
import com.devs.powerfit.dtos.programas.ProgramaDto;
import com.devs.powerfit.dtos.programas.ProgramaForListDto;
import com.devs.powerfit.enums.ENivelPrograma;
import com.devs.powerfit.enums.ESexo;
import com.devs.powerfit.interfaces.programas.IProgramaService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.programaMapper.ProgramaMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class ProgramaService implements IProgramaService {

    private ProgramaMapper _mapper;
    private ProgramaDao _repository;

    @Autowired
    public ProgramaService(ProgramaMapper mapper, ProgramaDao repository){
        _mapper = mapper;
        _repository = repository;
    }

    @Override
    public ProgramaDto create(ProgramaDto programaDto) {
        var crearProgramaDto = (CrearAndUpdateProgramaDto) programaDto;
        ProgramaBean newPrograma = _mapper.toBean(crearProgramaDto);
        newPrograma.setActive(true);
        _repository.save(newPrograma);
        return _mapper.toCreateAndUpdateDto(newPrograma);
    }

    @Override
    public ProgramaDto getById(Long id) {
        var programa = _repository.findByIdAndActiveTrue(id);
        if(programa.isEmpty()) return null;
        var programaDto = _mapper.toFullDto(programa.get());
        return programaDto;
    }

    @Override
    public PageResponse<ProgramaDto> getAll(int page) {
        return null;
    }

    @Override
    public PageResponse<ProgramaForListDto> getAll(int page, String titulo, ENivelPrograma nivel, ESexo sexo) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        Page<ProgramaForListDto> programas = _repository.findAll(titulo, nivel, sexo, pag);
        var pageResponse = new PageResponse<ProgramaForListDto>(
                programas.getContent(),
                programas.getTotalPages(),
                programas.getTotalElements(),
                programas.getNumber() + 1);

        return pageResponse;
    }

    @Override
    public ProgramaDto update(Long id, ProgramaDto programaDto) {
        var programa = _repository.findByIdAndActiveTrue(id);
        if(programa.isEmpty()) return null;
        var updated = (CrearAndUpdateProgramaDto) programaDto;
        var updatedBean = _mapper.toBean(updated);

        if(programa.isEmpty()) return null;

        if(updated.getEmpleado() != null){
            programa.get().setEmpleado(updatedBean.getEmpleado());
        }

        if(updated.getActividad()!=null){
            programa.get().setActividad(updatedBean.getActividad());
        }

        if(updatedBean.getTitulo()!=null){
            programa.get().setTitulo(updatedBean.getTitulo());
        }

        if(updatedBean.getNivel()!=null){
            programa.get().setNivel(updatedBean.getNivel());
        }

        if(updatedBean.getSexo()!=null){
            programa.get().setSexo(updatedBean.getSexo());
        }

        _repository.save(programa.get());
        return _mapper.toCreateAndUpdateDto(programa.get());
    }

    @Override
    public boolean delete(Long id) {
        var programa = _repository.findByIdAndActiveTrue(id);
        if(programa.isEmpty()) return false;
        programa.get().setActive(false);
        _repository.save(programa.get());
        return true;
    }


}
