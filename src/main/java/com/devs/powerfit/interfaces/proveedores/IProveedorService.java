package com.devs.powerfit.interfaces.proveedores;

import com.devs.powerfit.dtos.proveedores.ProveedorDto;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.utils.responses.PageResponse;

public interface IProveedorService extends IService<ProveedorDto> {
    PageResponse<ProveedorDto> searchByNombre(String nombre, int page);
    PageResponse<ProveedorDto> searchByRuc(String ruc, int page);

    public ProveedorDto getByRuc(String ruc);
}
