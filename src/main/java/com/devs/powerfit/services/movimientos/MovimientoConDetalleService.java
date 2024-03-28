package com.devs.powerfit.services.movimientos;
import com.devs.powerfit.daos.cajas.SesionCajaDao;
import com.devs.powerfit.dtos.movimientos.MovimientoConDetalleDto;
import com.devs.powerfit.dtos.movimientos.MovimientoDetalleDto;
import com.devs.powerfit.dtos.movimientos.MovimientoDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.interfaces.movimientos.IMovimientoConDetalleService;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovimientoConDetalleService implements IMovimientoConDetalleService {
    private final MovimientoDetalleService movimientoDetalleService;
    private final MovimientoService movimientoService;
    private final SesionCajaDao sesionCajaDao;

    @Autowired
    public MovimientoConDetalleService(MovimientoDetalleService movimientoDetalleService, MovimientoService movimientoService, SesionCajaDao sesionCajaDao) {
        this.movimientoDetalleService = movimientoDetalleService;
        this.movimientoService = movimientoService;
        this.sesionCajaDao = sesionCajaDao;
    }

    @Override
    public MovimientoConDetalleDto create(MovimientoConDetalleDto movimientoConDetalleDto) {
        MovimientoDto movimientoDto=movimientoConDetalleDto.getMovimiento();
        List<MovimientoDetalleDto> detalles=movimientoConDetalleDto.getDetalles();
        if (!comprobarTotal(movimientoDto,detalles)){
            throw new BadRequestException("No concuerda el total con los montos");
        }
        // Crear la factura principal
        MovimientoDto movimientoCreado = movimientoService.create(movimientoDto);

        // Crear los detalles de la factura
        List<MovimientoDetalleDto> detallesCreados = detalles.stream()
                .map(detalle -> {
                    detalle.setMovimientoId(movimientoCreado.getId()); // Asignar el ID del movimiento principal al detalle
                    return movimientoDetalleService.create(detalle);
                })
                .collect(Collectors.toList());
        MovimientoConDetalleDto nuevo = new MovimientoConDetalleDto();
        nuevo.setMovimiento(movimientoCreado);
        nuevo.setDetalles(detallesCreados);

        // Devolver el movimiento con los detalles creados
        return nuevo;
    }

    @Override
    public MovimientoConDetalleDto getById(Long id) {
        return null;
    }

    @Override
    public PageResponse<MovimientoConDetalleDto> getAll(int page) {
        return null;
    }

    @Override
    public MovimientoConDetalleDto update(Long id, MovimientoConDetalleDto movimientoConDetalleDto) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
    private boolean comprobarTotal(MovimientoDto movimiento,List<MovimientoDetalleDto>detalles){
        Double sumaMontos= detalles.stream()
                .mapToDouble(MovimientoDetalleDto::getMonto)
                .sum();
        return Double.compare(movimiento.getTotal(),sumaMontos)==0;
    }

    @Override
    public List<MovimientoConDetalleDto> findAllBySesionId(Long sesionId) {
        var sesionOptional = sesionCajaDao.findByIdAndActiveTrue(sesionId);
        if (sesionOptional.isEmpty()) {
            throw new BadRequestException("No existe sesion con ese id");
        }
        var movimientos = movimientoService.getAllBySesionId(sesionId);

        List<MovimientoConDetalleDto> movimientosConDetalle = new ArrayList<>();
        for (MovimientoDto movimiento : movimientos) {
            var detalles = movimientoDetalleService.findAllByMovimiento(movimiento.getId());
            MovimientoConDetalleDto item = new MovimientoConDetalleDto();
            item.setMovimiento(movimiento);
            item.setDetalles(detalles);
            movimientosConDetalle.add(item);
        }
        return movimientosConDetalle;
    }
}
