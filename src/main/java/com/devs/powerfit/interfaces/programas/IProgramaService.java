package com.devs.powerfit.interfaces.programas;

import com.devs.powerfit.dtos.programas.ProgramaDto;
import com.devs.powerfit.dtos.programas.ProgramaForListDto;
import com.devs.powerfit.dtos.programas.ProgramaItemDto;
import com.devs.powerfit.enums.ENivelPrograma;
import com.devs.powerfit.enums.ESexo;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.utils.responses.PageResponse;

public interface IProgramaService extends IService<ProgramaDto> {
    public PageResponse<ProgramaForListDto> getAll(int page, String titulo, ENivelPrograma nivel, ESexo sexo);
    public ProgramaItemDto createItem(Long programaId, ProgramaItemDto itemDto);
    public ProgramaItemDto getItemById(Long programaId, Long itemId);

    public PageResponse<ProgramaItemDto> getItemsByProgramaId(Long programaId, int page);

    public ProgramaItemDto updateItem(Long programaId, Long itemId, ProgramaItemDto itemDto);

    public boolean deleteItem(Long programaId, Long itemId);

}
