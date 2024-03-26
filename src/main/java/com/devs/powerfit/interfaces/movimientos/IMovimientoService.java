package com.devs.powerfit.interfaces.movimientos;

import com.devs.powerfit.dtos.facturas.FacturaDto;
import com.devs.powerfit.dtos.facturas.FacturaProveedorDto;
import com.devs.powerfit.dtos.movimientos.MovimientoDto;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.utils.responses.PageResponse;

import java.util.Date;
import java.util.List;

public interface IMovimientoService extends IService<MovimientoDto> {
    PageResponse<MovimientoDto> searchBySesionId(Long sesionId, int page);
    List<MovimientoDto> getAllBySesionId(Long sesionId);
    PageResponse<MovimientoDto> searchBySesion(int page, Date fechaMenor, Date fechaMayor);
    PageResponse<MovimientoDto> searchBySesionAndEntrada(int page, Long sesionId, boolean entrada);
    List<MovimientoDto> getAllBySesionAndEntrada(Long sesionId, boolean entrada);
    PageResponse<MovimientoDto> searchByFactura(int page, FacturaDto factura);
    List<MovimientoDto> getAllByFactura(FacturaDto factura);
    PageResponse<MovimientoDto> searchByFacturaProveedor(int page, FacturaProveedorDto facturaProveedor);
    List<MovimientoDto> getAllByFacturaProveedor(FacturaProveedorDto facturaProveedor);
}
