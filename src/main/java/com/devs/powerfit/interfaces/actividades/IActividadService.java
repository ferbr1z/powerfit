package com.devs.powerfit.interfaces.actividades;

import com.devs.powerfit.dtos.actividades.ActividadDto;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.utils.responses.PageResponse;

public interface IActividadService extends IService<ActividadDto> {
    PageResponse<ActividadDto> searchByNombre(String nombre, int page);
}
