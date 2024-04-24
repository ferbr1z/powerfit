package com.devs.powerfit.services.programas;

import com.devs.powerfit.beans.programas.ProgramaBean;
import com.devs.powerfit.daos.programas.ProgramaDao;
import com.devs.powerfit.daos.programas.ProgramaItemDao;
import com.devs.powerfit.dtos.programas.CrearAndUpdateProgramaDto;
import com.devs.powerfit.dtos.programas.ProgramaDto;
import com.devs.powerfit.dtos.programas.ProgramaForListDto;
import com.devs.powerfit.dtos.programas.ProgramaItemDto;
import com.devs.powerfit.enums.ENivelPrograma;
import com.devs.powerfit.enums.ESexo;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.programas.IProgramaService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.programaMapper.ProgramaItemMapper;
import com.devs.powerfit.utils.mappers.programaMapper.ProgramaMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class ProgramaService implements IProgramaService {

    private ProgramaMapper _mapper;
    private ProgramaItemMapper _itemMapper;
    private ProgramaDao _repository;
    private ProgramaItemDao _itemRepository;

    @Autowired
    public ProgramaService(ProgramaMapper mapper, ProgramaItemMapper itemMapper, ProgramaDao repository, ProgramaItemDao itemRepository){
        _mapper = mapper;
        _itemMapper = itemMapper;
        _repository = repository;
        _itemRepository = itemRepository;
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

    /***
     * ACA VA TODO LO DE LOS ITEMS
     */

    @Override
    public ProgramaItemDto createItem(Long programaId, ProgramaItemDto itemDto) {
        var programa = _repository.findByIdAndActiveTrue(programaId);
        if(programa.isEmpty()) throw new NotFoundException("Programa no encontrado");
        var item = _itemMapper.toBean(itemDto);
        item.setPrograma(programa.get());
        item.setActive(true);
        _itemRepository.save(item);
        return _itemMapper.toDto(item);
    }

    @Override
    public ProgramaItemDto getItemById(Long programaId, Long itemId) {
        var item = _itemRepository.findByIdAndActiveTrue(itemId);
        if(item.isEmpty()) throw new NotFoundException("Item no encontrado");
        return _itemMapper.toDto(item.get());
    }

    @Override
    public PageResponse<ProgramaItemDto> getItemsByProgramaId(Long programaId, int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var items = _itemRepository.findAllByProgramaId(pag, programaId);

        var itemsDto = items.map(item -> _itemMapper.toDto(item));

        var pageResponse = new PageResponse<ProgramaItemDto>(
                itemsDto.getContent(),
                items.getTotalPages(),
                items.getTotalElements(),
                items.getNumber() + 1);
        return pageResponse;
    }

    @Override
    public ProgramaItemDto updateItem(Long programaId, Long itemId, ProgramaItemDto itemDto) {
        var item = _itemRepository.findByIdAndActiveTrue(itemId);
        if(item.isEmpty()) throw new NotFoundException("Item no encontrado");

        var programa = _repository.findByIdAndActiveTrue(programaId);
        if(programa.isEmpty()) throw new NotFoundException("Programa no encontrado");

        if(itemDto.getNombre()!=null) item.get().setNombre(itemDto.getNombre());
        if(itemDto.getDescripcion()!=null) item.get().setDescripcion(itemDto.getDescripcion());
        if(itemDto.getTiempo()!=null) item.get().setTiempo(itemDto.getTiempo());
        if(itemDto.getRepeticiones()!=null) item.get().setRepeticiones(itemDto.getRepeticiones());

        _itemRepository.save(item.get());
        return _itemMapper.toDto(item.get());
    }

    @Override
    public boolean deleteItem(Long programaId, Long itemId) {
        var item = _itemRepository.findByIdAndActiveTrue(itemId);
        if(item.isEmpty()) throw new NotFoundException("Item no encontrado");
        item.get().setActive(false);
        _itemRepository.save(item.get());
        return true;
    }

}
