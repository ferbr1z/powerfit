package com.devs.powerfit.interfaces.programas;

import com.devs.powerfit.dtos.programas.clientePrograma.ClienteProgramaItemDto;
import com.devs.powerfit.utils.responses.PageResponse;

public interface IProgramaClienteItemService {
    public ClienteProgramaItemDto create(Long programaId, Long clienteProgramaId, ClienteProgramaItemDto clienteProgramaItemDto);
    public ClienteProgramaItemDto getById(Long programaId, Long clienteProgramaId, Long id);
    public PageResponse<ClienteProgramaItemDto> getAll(Long programaId, Long clienteProgramaId, int page);
    public ClienteProgramaItemDto update(Long programaId, Long clienteProgramaId, Long id, ClienteProgramaItemDto clienteProgramaItemDto);
    public boolean delete(Long programaId, Long clienteProgramaId, Long id);
}
