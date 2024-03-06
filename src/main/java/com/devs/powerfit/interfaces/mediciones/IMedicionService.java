package com.devs.powerfit.interfaces.mediciones;

import com.devs.powerfit.dtos.mediciones.MedicionDto;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.utils.responses.PageResponse;

import java.util.List;

public interface IMedicionService extends IService<MedicionDto> {
    PageResponse<MedicionDto> searchByNombreCliente(String nombre, int page);
    PageResponse<MedicionDto> searchByCiCliente(int ci, int page);
    PageResponse<MedicionDto> searchByIdCliente(Long id, int page);
}
