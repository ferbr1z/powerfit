package com.devs.powerfit.interfaces.programas;

import com.devs.powerfit.dtos.programas.clientePrograma.BaseClienteProgramDto;
import com.devs.powerfit.dtos.programas.clientePrograma.ClienteProgramaDto;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.utils.responses.PageResponse;

import java.time.LocalDate;

public interface IProgramaClienteService {
    public BaseClienteProgramDto registrarCliente(Long programaId, BaseClienteProgramDto clienteProgramaDto);
    public ClienteProgramaDto getClienteProgramaById(Long programaId, Long id);
    public PageResponse<ClienteProgramaDto> getClientesByProgramaId(Long programaId, String nombreCliente, LocalDate fechaInicio, LocalDate fechaFin, int page);
    public PageResponse<ClienteProgramaDto> getAllByClienteEmail(String clienteEmail, LocalDate fechaInicio, LocalDate fechaFin, int page);
    public ClienteProgramaDto updateClientePrograma(Long programaId, Long id, ClienteProgramaDto clienteProgramaDto);
    public boolean deleteClientePrograma(Long programaId, Long id);
}
