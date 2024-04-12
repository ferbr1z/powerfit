package com.devs.powerfit.interfaces.facturas;

import com.devs.powerfit.dtos.facturas.FacturaConDetallesDto;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.utils.responses.PageResponse;

import java.time.LocalDate;

public interface IFacturaConDetallesService extends IService<FacturaConDetallesDto> {
    PageResponse<FacturaConDetallesDto> getAllByNombreCliente(String nombre, int page);
    PageResponse<FacturaConDetallesDto> getAllByRucCliente(String ruc, int page);
    FacturaConDetallesDto getByNumeroFactura(String numero);
    PageResponse<FacturaConDetallesDto> searchByPagado(int page);
    PageResponse<FacturaConDetallesDto> searchByPendiente(int page);

    PageResponse<FacturaConDetallesDto> searchByFecha(int page, LocalDate fechaInicio, LocalDate fechaFin);
}
