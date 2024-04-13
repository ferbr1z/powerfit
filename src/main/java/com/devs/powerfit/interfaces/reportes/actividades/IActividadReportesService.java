package com.devs.powerfit.interfaces.reportes.actividades;

import com.devs.powerfit.dtos.reportes.actividades.ActividadReportesDto;
import com.devs.powerfit.enums.EModalidad;
import com.devs.powerfit.utils.responses.PageResponse;

import java.util.List;

public interface IActividadReportesService {
    public List<ActividadReportesDto> getActividadesConMasSuscripciones();
    public List<ActividadReportesDto> getActividadesMasRenovadas();
    public List<ActividadReportesDto> getActividadesConMasSuscripcionesPorModalidad(EModalidad estado);

}
