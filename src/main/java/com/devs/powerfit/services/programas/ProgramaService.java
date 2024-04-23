package com.devs.powerfit.services.programas;

import com.devs.powerfit.beans.programas.ProgramaBean;
import com.devs.powerfit.daos.programas.ProgramaDao;
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
        ProgramaBean newPrograma = _mapper.toBean(programaDto);
        newPrograma.setActive(true);
        _repository.save(newPrograma);
        return _mapper.toCreateDto(newPrograma);
    }

    @Override
    public ProgramaDto getById(Long id) {
        return null;
    }

    @Override
    public PageResponse<ProgramaDto> getAll(int page) {
        return null;
    }

    @Override
    public PageResponse<ProgramaForListDto> getAll(int page, String titulo, ENivelPrograma nivel, ESexo sexo) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        Page<ProgramaForListDto> programas = _repository.findAll(titulo, nivel, sexo, pag);
//        var programas = _repository.findAll(pag);
//        var programasDto = programas.map(programa -> _mapper.toDto(programa));
        var pageResponse = new PageResponse<ProgramaForListDto>(
                programas.getContent(),
                programas.getTotalPages(),
                programas.getTotalElements(),
                programas.getNumber() + 1);

        return pageResponse;
    }

    @Override
    public ProgramaDto update(Long id, ProgramaDto programaDto) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }


}
