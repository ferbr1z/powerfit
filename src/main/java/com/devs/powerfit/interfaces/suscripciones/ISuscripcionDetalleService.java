package com.devs.powerfit.interfaces.suscripciones;

import com.devs.powerfit.dtos.suscripciones.SuscripcionDto;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.utils.responses.PageResponse;

public interface ISuscripcionDetalleService extends IService<SuscripcionDto> {
    PageResponse<SuscripcionDto> getAllByClienteEmail(String email, int page);
}
