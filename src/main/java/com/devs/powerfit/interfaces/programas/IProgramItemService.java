package com.devs.powerfit.interfaces.programas;

import com.devs.powerfit.dtos.programas.ProgramaItemDto;
import com.devs.powerfit.utils.responses.PageResponse;

public interface IProgramItemService {
    public ProgramaItemDto createItem(Long programaId, ProgramaItemDto itemDto);
    public ProgramaItemDto getItemById(Long programaId, Long itemId);
    public PageResponse<ProgramaItemDto> getItemsByProgramaId(Long programaId, int page);
    public ProgramaItemDto updateItem(Long programaId, Long itemId, ProgramaItemDto itemDto);
    public boolean deleteItem(Long programaId, Long itemId);
}
