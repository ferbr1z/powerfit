package com.devs.powerfit.services.programas;

import com.devs.powerfit.beans.programas.ProgramaBean;
import com.devs.powerfit.daos.programas.ProgramaDao;
import com.devs.powerfit.daos.programas.ProgramaItemDao;
import com.devs.powerfit.dtos.programas.*;
import com.devs.powerfit.enums.ENivelPrograma;
import com.devs.powerfit.enums.ESexo;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.actividades.IActividadService;
import com.devs.powerfit.interfaces.empleados.IEmpleadoService;
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
    private IEmpleadoService _empleadoService;
    private IActividadService _actividadService;
    private ProgramaDao _repository;
    private ProgramaItemDao _itemRepository;

    @Autowired
    public ProgramaService(ProgramaMapper mapper,
                           ProgramaDao repository,
                           ProgramaItemDao itemRepository,
                           IEmpleadoService empleadoService,
                           IActividadService actividadService){
        _mapper = mapper;
        _repository = repository;
        _itemRepository = itemRepository;
        _empleadoService = empleadoService;
        _actividadService = actividadService;
    }

    @Override
    public ProgramaDto create(ProgramaDto programaDto) {
        var crearProgramaDto = (CrearAndUpdateProgramaDto) programaDto;
        var entrenadorId = crearProgramaDto.getEmpleado();
        var actividadId = crearProgramaDto.getActividad();

        if(_empleadoService.getById(entrenadorId)==null) throw new NotFoundException("Entrenador no encontrado");
        if(_actividadService.getById(actividadId)==null) throw new NotFoundException("Actividad no encontrada");

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

        var items = _itemRepository.getAllByProgramaIdAAndActiveTrue(id);

        for (var item: items) {
            item.setActive(false);
            _itemRepository.save(item);
        }

        programa.get().setActive(false);
        _repository.save(programa.get());
        return true;
    }

    @Override
    public PageResponse<CantClientesProgramaDto> getCantClientesByEntrenadorPrograma(Long entrenadorId, int page){
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        Page<CantClientesProgramaDto> programas = _repository.findAllByEmpleadoId(entrenadorId, pag);
        var pageResponse = new PageResponse<CantClientesProgramaDto>(
                programas.getContent(),
                programas.getTotalPages(),
                programas.getTotalElements(),
                programas.getNumber() + 1);

        return pageResponse;
    }


}
