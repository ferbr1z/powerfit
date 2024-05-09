package com.devs.powerfit.interfaces.clientes;

import com.devs.powerfit.dtos.clientes.ClienteDto;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.utils.responses.PageResponse;


public interface IClienteService extends IService<ClienteDto> {
    PageResponse<ClienteDto> searchByNombre(String nombre, int page);
    PageResponse<ClienteDto> searchByCi(String ci, int page);

    PageResponse<ClienteDto> searchByRuc(String ruc, int page);

    String createAccountsForClientsWithoutUsuario();
}
