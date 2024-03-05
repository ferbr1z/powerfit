package com.devs.powerfit.interfaces.mediciones;

import com.devs.powerfit.dtos.mediciones.MedicionDto;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.utils.responses.PageResponse;

public interface IMedicionService extends IService<MedicionDto> {
    PageResponse<MedicionDto> searchByNombreCliente(String nombre, int page);
}
