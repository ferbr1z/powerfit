package com.devs.powerfit.interfaces.cajas;

import com.devs.powerfit.dtos.cajas.CajaDto;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.utils.responses.PageResponse;

public interface ICajaService extends IService<CajaDto> {
    PageResponse<CajaDto> searchByNombre(String nombre, int page);
}
