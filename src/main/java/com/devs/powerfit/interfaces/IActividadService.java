package com.devs.powerfit.interfaces;

import com.devs.powerfit.dtos.ActividadDto;
import com.devs.powerfit.dtos.PageResponse;

public interface IActividadService extends IService<ActividadDto> {
    PageResponse<ActividadDto> searchByNombre(String nombre, int page);
}
