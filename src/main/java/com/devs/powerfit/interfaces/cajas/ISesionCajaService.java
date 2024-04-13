package com.devs.powerfit.interfaces.cajas;

import com.devs.powerfit.dtos.cajas.SesionCajaDto;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.utils.responses.PageResponse;

import java.time.LocalDate;
import java.util.List;

public interface ISesionCajaService extends IService<SesionCajaDto> {
    PageResponse<SesionCajaDto> searchByFecha(int page, LocalDate fechaInicio, LocalDate fechaFin);
}
