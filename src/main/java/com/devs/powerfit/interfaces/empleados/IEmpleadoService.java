package com.devs.powerfit.interfaces.empleados;

import com.devs.powerfit.dtos.empleados.EmpleadoDto;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.utils.responses.PageResponse;

public interface IEmpleadoService extends IService<EmpleadoDto> {
    PageResponse<EmpleadoDto> searchByNombre(String nombre, int page);
    PageResponse<EmpleadoDto> searchByRolId(Long id, int page);
}
