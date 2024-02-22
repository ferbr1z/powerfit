package com.devs.powerfit.interfaces;

import com.devs.powerfit.dtos.ClienteDto;
import com.devs.powerfit.dtos.PageResponse;
import org.springframework.stereotype.Component;


public interface IClienteService extends IService<ClienteDto>{
    PageResponse<ClienteDto> searchByNombre(String nombre, int page);
    PageResponse<ClienteDto> searchByCi(String ci, int page);
}
