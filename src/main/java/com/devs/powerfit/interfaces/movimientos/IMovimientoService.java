package com.devs.powerfit.interfaces.movimientos;

import com.devs.powerfit.dtos.facturas.FacturaDto;
import com.devs.powerfit.dtos.facturas.FacturaProveedorDto;
import com.devs.powerfit.dtos.movimientos.MovimientoConDetalleDto;
import com.devs.powerfit.dtos.movimientos.MovimientoDto;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.utils.responses.PageResponse;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface IMovimientoService extends IService<MovimientoDto> {
    PageResponse<MovimientoDto> searchBySesionId(Long sesionId, int page);
    List<MovimientoDto> getAllBySesionId(Long sesionId);
    PageResponse<MovimientoDto> searchByFecha(int page, Date fechaMenor, Date fechaMayor);
    PageResponse<MovimientoDto> searchBySesionAndEntrada(int page, Long sesionId, boolean entrada);
    List<MovimientoDto> getAllBySesionAndEntrada(Long sesionId, boolean entrada);
    PageResponse<MovimientoDto> searchByComprobanteNombre(int page, String nombre);
    PageResponse<MovimientoDto> searchFacturaByNombreComprobanteAndEntradaAndFechaBetweenAndNombreCaja(int page, String nombre, Boolean entrada, LocalDate fechaInicio,LocalDate fechaFin,String nombreCaja);
    PageResponse<MovimientoDto> searchFacturaProveedorByNombreComprobanteAndEntradaAndFechaBetweenAndNombreCaja(int page, String nombre, Boolean entrada, LocalDate fechaInicio,LocalDate fechaFin,String nombreCaja);

}
