package com.devs.powerfit.interfaces.cajas;

import com.devs.powerfit.dtos.cajas.ExtraccionDeCajaDto;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.utils.responses.PageResponse;

import java.time.LocalDate;

public interface IExtraccionCajaService extends IService<ExtraccionDeCajaDto> {
    public PageResponse<ExtraccionDeCajaDto> getAllBetween(int page, LocalDate fechaInicio, LocalDate fechaFin);
}
