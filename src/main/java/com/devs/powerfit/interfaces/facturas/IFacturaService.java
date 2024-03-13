package com.devs.powerfit.interfaces.facturas;

import com.devs.powerfit.dtos.facturas.FacturaDto;
import com.devs.powerfit.dtos.mediciones.MedicionDto;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.utils.responses.PageResponse;

import java.util.Date;

public interface IFacturaService extends IService<FacturaDto> {
    PageResponse<FacturaDto> searchByNombreCliente(String nombre, int page);
    PageResponse<FacturaDto> searchByRucCliente(String nombre, int page);
    FacturaDto searchByNumeroFactura(String numeroFactura);


}
