package com.devs.powerfit.interfaces.programas;

import com.devs.powerfit.dtos.programas.clientePrograma.ClienteProgramaDto;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.utils.responses.PageResponse;

public interface IProgramaClienteService {
    public ClienteProgramaDto registrarCliente(Long programaId, ClienteProgramaDto clienteProgramaDto);
    public ClienteProgramaDto getClienteProgramaById(Long programaId, Long id);
    public PageResponse<ClienteProgramaDto> getClientesByProgramaId(Long programaId, int page);
    public ClienteProgramaDto updateClientePrograma(Long programaId, Long id, ClienteProgramaDto clienteProgramaDto);
    public boolean deleteClientePrograma(Long programaId, Long id);
}
