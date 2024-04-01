package com.devs.powerfit.services.movimientos;

import com.devs.powerfit.beans.movimientos.MovimientoBean;
import com.devs.powerfit.beans.movimientos.MovimientoDetalleBean;
import com.devs.powerfit.daos.movimientos.MovimientoDao;
import com.devs.powerfit.daos.movimientos.MovimientoDetalleDao;
import com.devs.powerfit.dtos.movimientos.MovimientoDetalleDto;
import com.devs.powerfit.dtos.tiposDePagos.TipoDePagoDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.movimientos.IMovimientoDetalleService;
import com.devs.powerfit.services.tiposDePago.TipoDePagoService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.movimientoMappers.MovimientoDetalleMapper;
import com.devs.powerfit.utils.mappers.movimientoMappers.MovimientoMapper;
import com.devs.powerfit.utils.mappers.tiposDePagoMapper.TipoDePagoMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovimientoDetalleService implements IMovimientoDetalleService {
    private final MovimientoDao movimientoDao;
    private final MovimientoDetalleDao dao;
    private final MovimientoMapper movimientoMapper;
    private final MovimientoDetalleMapper mapper;
    private final TipoDePagoMapper tipoDePagoMapper;
    private final TipoDePagoService tipoDePagoService;
    @Autowired
    public MovimientoDetalleService(MovimientoDao movimientoDao, MovimientoDetalleDao dao, MovimientoMapper movimientoMapper, MovimientoDetalleMapper mapper, TipoDePagoMapper tipoDePagoMapper, TipoDePagoService tipoDePagoService) {
        this.movimientoDao = movimientoDao;
        this.dao = dao;
        this.movimientoMapper = movimientoMapper;
        this.mapper = mapper;
        this.tipoDePagoMapper = tipoDePagoMapper;
        this.tipoDePagoService = tipoDePagoService;
    }
    @Override
    public MovimientoDetalleDto create(MovimientoDetalleDto movimientoDetalleDto) {
        // Verificar si existe el movimiento
        Optional<MovimientoBean> optionalMovimiento = movimientoDao.findByIdAndActiveTrue(movimientoDetalleDto.getMovimientoId());
        if (optionalMovimiento.isEmpty()) {
            throw new BadRequestException("El movimiento especificado no existe o está inactivo.");
        }
        MovimientoBean movimiento = optionalMovimiento.get();

        // Verificar si existe el tipo de pago
        TipoDePagoDto tipoDePago = tipoDePagoService.getById(movimientoDetalleDto.getTipoDePagoId());
        if (tipoDePago == null) {
            throw new BadRequestException("El tipo de pago especificado no existe.");
        }

        // Crear el detalle del movimiento
        MovimientoDetalleBean detalle = new MovimientoDetalleBean();
        detalle.setMovimiento(movimiento);
        detalle.setTipoDePago(tipoDePagoMapper.toBean(tipoDePago));
        detalle.setMonto(movimientoDetalleDto.getMonto());
        detalle.setActive(true);
        // Guardar el detalle del movimiento en la base de datos
        MovimientoDetalleBean detalleCreado = dao.save(detalle);

        // Retornar el DTO del detalle creado
        return mapper.toDto(detalleCreado);
    }


    @Override
    public MovimientoDetalleDto getById(Long id) {
        var movimiento = dao.findByIdAndActiveTrue(id);
        if (movimiento.isPresent()) {
            return mapper.toDto(movimiento.get());
        }
        throw new NotFoundException("Movimiento no encontrado");
    }

    @Override
    public PageResponse<MovimientoDetalleDto> getAll(int page) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var movimientoPage = dao.findAllByActiveTrue(pageRequest);
        if (movimientoPage.isEmpty()) {
            throw new NotFoundException("No hay movimientos en la lista");
        }
        var movimientoDtoPage = movimientoPage.map(mapper::toDto);
        return new PageResponse<>(movimientoDtoPage.getContent(),
                movimientoDtoPage.getTotalPages(),
                movimientoDtoPage.getTotalElements(),
                movimientoDtoPage.getNumber() + 1);
    }

    @Override
    public MovimientoDetalleDto update(Long id, MovimientoDetalleDto movimientoDetalleDto) {
        //No se debe poder modificar un movimiento detalle
        return null;
    }

    @Override
    public boolean delete(Long id) {
        //No se debe eliminar un movimiento detalle;
        return false;
    }

    @Override
    public PageResponse<MovimientoDetalleDto> findAllByMovimiento(int page, Long movimientoId) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var movimientoBean = movimientoDao.findByIdAndActiveTrue(movimientoId);
        if (movimientoBean.isEmpty()) {
            throw new BadRequestException("No existe sesion con ese Id");
        }
        var movimientoPage = dao.findAllByMovimientoAndActiveTrue(pageRequest,movimientoBean.get());
        if (movimientoPage.isEmpty()) {
            throw new NotFoundException("No hay movimientos en la lista");
        }
        var movimientoDtoPage = movimientoPage.map(mapper::toDto);
        return new PageResponse<>(movimientoDtoPage.getContent(),
                movimientoDtoPage.getTotalPages(),
                movimientoDtoPage.getTotalElements(),
                movimientoDtoPage.getNumber() + 1);

    }

    @Override
    public List<MovimientoDetalleDto> findAllByMovimiento(Long movimientoId) {
        var movimiento = movimientoDao.findByIdAndActiveTrue(movimientoId);
        if (movimiento.isEmpty()) {
            throw new BadRequestException("No existe movimiento con ese id");
        }
        var detalles = dao.findAllByMovimientoAndActiveTrue(movimiento.get());
        return detalles.stream()
                .map(movimientoDetalleBean -> mapper.toDto(movimientoDetalleBean))
                .collect(Collectors.toList()); // Añade este colector para convertir el Stream a List
    }

    @Override
    public PageResponse<MovimientoDetalleDto> findAllByTipoDePago(int page, Long tipoDePagoId) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var sesion = tipoDePagoService.getById(tipoDePagoId);
        if (sesion==null) {
            throw new BadRequestException("No existe tipo de pago con ese Id");
        }
        var movimientoPage = dao.findAllByTipoDePagoAndActiveTrue(pageRequest,tipoDePagoMapper.toBean(sesion));
        if (movimientoPage.isEmpty()) {
            throw new NotFoundException("No hay movimientos en la lista");
        }
        var movimientoDtoPage = movimientoPage.map(mapper::toDto);
        return new PageResponse<>(movimientoDtoPage.getContent(),
                movimientoDtoPage.getTotalPages(),
                movimientoDtoPage.getTotalElements(),
                movimientoDtoPage.getNumber() + 1);
    }

    @Override
    public List<MovimientoDetalleDto> findAllByTipoDePago(Long tipoDePagoId) {
        var sesion = tipoDePagoService.getById(tipoDePagoId);
        if (sesion==null) {
            throw new BadRequestException("No existe tipo de pago con ese Id");
        }
        var movimientos = dao.findAllByTipoDePagoAndActiveTrue(tipoDePagoMapper.toBean(sesion));
        return movimientos.stream()
                .map(movimientoBean -> mapper.toDto(movimientoBean))
                .collect(Collectors.toList());
    }
}
