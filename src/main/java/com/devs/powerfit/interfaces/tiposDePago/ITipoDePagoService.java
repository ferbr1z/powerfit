package com.devs.powerfit.interfaces.tiposDePago;

import com.devs.powerfit.dtos.tiposDePagos.TipoDePagoDto;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.utils.responses.PageResponse;

public interface ITipoDePagoService extends IService<TipoDePagoDto> {
    PageResponse<TipoDePagoDto> searchByNombre(String nombre, int page);
}
