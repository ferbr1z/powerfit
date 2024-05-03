package com.devs.powerfit.services.programas;

import com.devs.powerfit.daos.programas.ProgramaDao;
import com.devs.powerfit.daos.programas.ProgramaItemDao;
import com.devs.powerfit.dtos.programas.ProgramaItemDto;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.programas.IProgramItemService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.programaMapper.ProgramaItemMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ProgramaItemService implements IProgramItemService {
    private ProgramaDao _repository;

    private ProgramaItemMapper _itemMapper;
    private ProgramaItemDao _itemRepository;

    @Autowired
    public ProgramaItemService(ProgramaItemMapper itemMapper, ProgramaItemDao itemRepository, ProgramaDao repository){
        _itemMapper = itemMapper;
        _itemRepository = itemRepository;
        _repository = repository;
    }


    @Override
    public ProgramaItemDto createItem(Long programaId, ProgramaItemDto itemDto) {
        if(itemDto.getPeso() == null && itemDto.getRepeticiones() == null && itemDto.getTiempo() == null)
            throw new IllegalArgumentException("Debe enviar al menos un campo de peso, repeticiones o tiempo");

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
        var item = _itemRepository.findByIdAAndProgramaIdAndActiveTrue(itemId, programaId);
        if(item.isEmpty()) throw new NotFoundException("Item no encontrado");
        return _itemMapper.toDto(item.get());
    }

    @Override
    public PageResponse<ProgramaItemDto> getItemsByProgramaId(Long programaId, int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var items = _itemRepository.findAllByProgramaIdAndActiveTrue(pag, programaId);

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
