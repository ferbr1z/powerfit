package com.devs.powerfit.interfaces.suscripciones;

import com.devs.powerfit.dtos.clientes.ClienteDto;
import com.devs.powerfit.dtos.suscripciones.SuscripcionDto;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.utils.responses.PageResponse;

public interface ISuscripcionService extends IService<SuscripcionDto> {
    PageResponse<SuscripcionDto> searchByNombreCliente(String nombre, int page);
}
