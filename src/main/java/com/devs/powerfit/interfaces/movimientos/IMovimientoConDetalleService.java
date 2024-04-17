package com.devs.powerfit.interfaces.movimientos;

import com.devs.powerfit.dtos.facturas.FacturaConDetallesDto;
import com.devs.powerfit.dtos.movimientos.IngresosTotalesDto;
import com.devs.powerfit.dtos.movimientos.MovimientoConDetalleDto;
import com.devs.powerfit.dtos.movimientos.MovimientoDetalleDto;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.services.movimientos.MovimientoConDetalleService;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;

public interface IMovimientoConDetalleService extends IService<MovimientoConDetalleDto> {
    List<MovimientoConDetalleDto> findAllBySesionId(Long sesionId);
    PageResponse<MovimientoConDetalleDto> getAllBySesionId(int page, Long sesionId);
    PageResponse<MovimientoConDetalleDto> searchByComprobanteNombre(int page,String nombre);
    PageResponse<MovimientoConDetalleDto> searchMovimientoByNombreAndFacturaAndEntradaAndFechaBetweenAndNombreCaja(int page, String nombre, Boolean entrada, LocalDate fechaInicio,LocalDate fechaFin,String nombreCaja);
    PageResponse<MovimientoConDetalleDto> searchMovimientoByNombreAndFacturaProveedorAndEntradaAndFechaBetweenAndNombreCaja(int page, String nombre, Boolean entrada, LocalDate fechaInicio,LocalDate fechaFin,String nombreCaja);
    IngresosTotalesDto getByFechaBetween(LocalDate fechaInicio,LocalDate fechaFin);


}
