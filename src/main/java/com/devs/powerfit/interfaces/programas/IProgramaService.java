package com.devs.powerfit.interfaces.programas;

import com.devs.powerfit.dtos.programas.CantClientesProgramaDto;
import com.devs.powerfit.dtos.programas.ProgramaDto;
import com.devs.powerfit.dtos.programas.ProgramaForListDto;
import com.devs.powerfit.dtos.programas.ProgramaItemDto;
import com.devs.powerfit.dtos.programas.clientePrograma.ClienteProgramaDto;
import com.devs.powerfit.enums.ENivelPrograma;
import com.devs.powerfit.enums.ESexo;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.data.domain.Page;

public interface IProgramaService extends IService<ProgramaDto> {
    public PageResponse<ProgramaForListDto> getAll(int page, String titulo, ENivelPrograma nivel, ESexo sexo);


    public PageResponse<CantClientesProgramaDto> getCantClientesByEntrenadorPrograma(Long entrenadorId, int page);
}
