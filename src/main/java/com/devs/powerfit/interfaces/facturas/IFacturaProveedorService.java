package com.devs.powerfit.interfaces.facturas;

import com.devs.powerfit.dtos.facturas.FacturaDto;
import com.devs.powerfit.dtos.facturas.FacturaProveedorDto;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.utils.responses.PageResponse;

public interface IFacturaProveedorService extends IService<FacturaProveedorDto> {
    PageResponse<FacturaProveedorDto> searchByNombreProveedor(String nombre, int page);
    PageResponse<FacturaProveedorDto> searchByRucProveedor(String nombre, int page);
    FacturaProveedorDto searchByNumeroFactura(String numeroFactura);


}
