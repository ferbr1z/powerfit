package com.devs.powerfit.interfaces.facturas;

import com.devs.powerfit.dtos.facturas.FacturaDetalleDto;
import com.devs.powerfit.interfaces.bases.IService;

import java.util.Date;
import java.util.List;

public interface IFacturaDetalleService extends IService<FacturaDetalleDto> {

    List<FacturaDetalleDto> getAllDetalles();
    public List<FacturaDetalleDto> getAllDetallesBetween(Date fechaInicio, Date fechaFin);



}
