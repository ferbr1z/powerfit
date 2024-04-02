package com.devs.powerfit.services.movimientos;

import com.devs.powerfit.beans.cajas.SesionCajaBean;
import com.devs.powerfit.beans.movimientos.MovimientoBean;
import com.devs.powerfit.daos.cajas.SesionCajaDao;
import com.devs.powerfit.daos.movimientos.MovimientoDao;
import com.devs.powerfit.dtos.facturas.FacturaDto;
import com.devs.powerfit.dtos.facturas.FacturaProveedorDto;
import com.devs.powerfit.dtos.movimientos.MovimientoDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.movimientos.IMovimientoService;
import com.devs.powerfit.services.cajas.SesionCajaService;
import com.devs.powerfit.services.facturas.FacturaProveedorService;
import com.devs.powerfit.services.facturas.FacturaService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.CajaMappers.SesionCajaMapper;
import com.devs.powerfit.utils.mappers.facturaMappers.FacturaMapper;
import com.devs.powerfit.utils.mappers.facturaMappers.FacturaProveedorMapper;
import com.devs.powerfit.utils.mappers.movimientoMappers.MovimientoMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovimientoService implements IMovimientoService {
    private final MovimientoDao dao;
    private final SesionCajaService sesionCajaService;
    private final FacturaService facturaService;
    private final FacturaMapper facturaMapper;
    private SesionCajaDao sesionCajaDao;
    private final FacturaProveedorService facturaProveedorService;
    private final FacturaProveedorMapper facturaProveedorMapper;
    private final SesionCajaMapper sesionCajaMapper;
    private final MovimientoMapper mapper;

    @Autowired
    public MovimientoService(MovimientoDao dao, SesionCajaService sesionCajaService, FacturaService facturaService, FacturaMapper facturaMapper, SesionCajaDao sesionCajaDao, FacturaProveedorService facturaProveedorService, FacturaProveedorMapper facturaProveedorMapper, SesionCajaMapper sesionCajaMapper, MovimientoMapper mapper) {
        this.dao = dao;
        this.sesionCajaService = sesionCajaService;
        this.facturaService = facturaService;
        this.facturaMapper = facturaMapper;
        this.sesionCajaDao = sesionCajaDao;
        this.facturaProveedorService = facturaProveedorService;
        this.facturaProveedorMapper = facturaProveedorMapper;
        this.sesionCajaMapper = sesionCajaMapper;
        this.mapper = mapper;
    }

    @Override
    public MovimientoDto create(MovimientoDto movimientoDto) {
        // Verificar que sesionId sea válido
        Optional<SesionCajaBean> sesion = sesionCajaDao.findByIdAndActiveTrue(movimientoDto.getSesionId());
        if (sesion.isEmpty()) {
            throw new BadRequestException("SesionId debe ser válido.");
        }


        // Generar la fecha actual si no se proporciona
        if (movimientoDto.getFecha() == null) {
            movimientoDto.setFecha(new Date());
        }

        // Generar la hora actual si no se proporciona
        if (movimientoDto.getHora() == null) {
            movimientoDto.setHora(new Date());
        }

        // Validar y formatear la fecha
        SimpleDateFormat sdfFecha = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date fechaFormateada = sdfFecha.parse(sdfFecha.format(movimientoDto.getFecha()));
            movimientoDto.setFecha(fechaFormateada);
        } catch (ParseException e) {
            throw new BadRequestException("Formato de fecha inválido: " + movimientoDto.getFecha());
        }

        // Validar y formatear la hora
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");
        try {
            Date horaFormateada = sdfHora.parse(sdfHora.format(movimientoDto.getHora()));
            movimientoDto.setHora(horaFormateada);
        } catch (ParseException e) {
            throw new BadRequestException("Formato de hora inválido: " + movimientoDto.getHora());
        }

        // Verificar si es una entrada o salida
        if (movimientoDto.isEntrada()) {
            // Verificar que facturaId sea válido y facturaProveedorId sea nulo
            if (movimientoDto.getFacturaId() ==null) {
                throw new BadRequestException("FacturaId debe ser válido para una entrada.");
            }
            FacturaDto factura = facturaService.getById(movimientoDto.getFacturaId());
            if (factura == null) {
                throw new BadRequestException("La factura no existe");
            }
            if (factura.isPagado()) {
                throw new BadRequestException("La factura ya está pagada");
            }
            if (factura.getSaldo() < movimientoDto.getTotal()) {
                throw new BadRequestException("El saldo es menor al total del movimiento");
            }
            MovimientoBean movimiento = new MovimientoBean();
            movimiento.setActive(true);
            movimiento.setHora(movimientoDto.getHora());
            movimiento.setFecha(movimientoDto.getFecha());
            movimiento.setTotal(movimientoDto.getTotal());
            movimiento.setEntrada(movimientoDto.isEntrada());
            // Restar el total del movimiento al saldo de la factura
            factura.setSaldo(factura.getSaldo() - movimientoDto.getTotal());
            FacturaDto facturaActualizada = facturaService.actualizarSaldo(factura.getId(), factura.getSaldo());
            if (factura.getSaldo() == 0) {
                facturaActualizada = facturaService.modificarPagado(factura.getId(), true);
            }
            movimiento.setFactura(facturaMapper.toBean(facturaActualizada));
            movimiento.setSesion(sesion.get());
            if(!sesionCajaService.aumentarMontoCaja(movimiento.getSesion().getId(), movimiento.getTotal())){
                throw new BadRequestException("Ha ocurrido un error al momento de actualizar el monto de caja");
            }
            movimiento.setFacturaProveedor(null);
            MovimientoBean creado = dao.save(movimiento);
            return mapper.toDto(creado);

        } else {
            // Verificar que facturaProveedorId sea válido y facturaId sea nulo
            if (movimientoDto.getFacturaProveedorId() ==null ) {
                throw new BadRequestException("FacturaProveedorId debe ser válido y FacturaId debe ser nulo para una salida.");
            }
            FacturaProveedorDto facturaProveedor = facturaProveedorService.getById(movimientoDto.getFacturaProveedorId());
            System.out.println(facturaProveedor);
            if (facturaProveedor == null) {
                throw new BadRequestException("La factura del proveedor no existe");
            }
            if (facturaProveedor.getSaldo() < movimientoDto.getTotal()) {
                throw new BadRequestException("El saldo es menor al total del movimiento");
            }
            MovimientoBean movimiento = new MovimientoBean();
            movimiento.setActive(true);
            movimiento.setHora(movimientoDto.getHora());
            movimiento.setFecha(movimientoDto.getFecha());
            movimiento.setTotal(movimientoDto.getTotal());
            movimiento.setEntrada(movimientoDto.isEntrada());
            // Restar el total del movimiento al saldo de la factura del proveedor
            facturaProveedor.setSaldo(facturaProveedor.getSaldo() - movimientoDto.getTotal());
            if(facturaProveedor.getSaldo()==0){
                facturaProveedorService.modificarPagado(facturaProveedor.getId(),true);
            }
            FacturaProveedorDto facturaProveedorActualizada = facturaProveedorService.actualizarSaldo(facturaProveedor.getId(),facturaProveedor.getSaldo());
            movimiento.setFacturaProveedor(facturaProveedorMapper.toBean(facturaProveedorActualizada));
            movimiento.setSesion(sesion.get());
            if(!sesionCajaService.disminuirMontoCaja(movimiento.getSesion().getId(), movimiento.getTotal())){
                throw new BadRequestException("Ha ocurrido un error al momento de actualizar el monto de caja");
            }
            movimiento.setFactura(null);
            MovimientoBean creado = dao.save(movimiento);
            return mapper.toDto(creado);
        }
    }


    @Override
    public MovimientoDto getById(Long id) {
        var movimiento = dao.findByIdAndActiveTrue(id);
        if (movimiento.isPresent()) {
            return mapper.toDto(movimiento.get());
        }
        throw new NotFoundException("Movimiento no encontrado");
    }

    @Override
    public PageResponse<MovimientoDto> getAll(int page) {
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
    public MovimientoDto update(Long id, MovimientoDto movimientoDto) {
        //No se debe actualizar un movimiento.
        return null;
    }

    @Override
    public boolean delete(Long id) {
        //No se debe eliminar un movimiento
        return false;
    }
    @Override
    public PageResponse<MovimientoDto> searchBySesionId(Long sesionId, int page) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var sesion = sesionCajaDao.findByIdAndActiveTrue(sesionId);
        if (sesion.isEmpty()) {
            throw new BadRequestException("No existe sesion con ese Id");
        }
        var movimientoPage = dao.findAllBySesionAndActiveTrue(pageRequest,sesion.get());
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
    public List<MovimientoDto> getAllBySesionId(Long sesionId) {
        var sesion = sesionCajaDao.findByIdAndActiveTrue(sesionId);
        if (sesion.isEmpty()) {
            throw new BadRequestException("No existe sesion con ese Id");
        }
        var movimientos = dao.findAllBySesionAndActiveTrue(sesion.get());
        return movimientos.stream()
                .map(movimientoBean -> mapper.toDto(movimientoBean))
                .collect(Collectors.toList());
    }






    @Override
    public PageResponse<MovimientoDto> searchByFecha(int page, Date fechaMenor, Date fechaMayor) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var movimientoPage = dao.findAllByFechaBetween(pageRequest,fechaMenor,fechaMayor);
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
    public PageResponse<MovimientoDto> searchBySesionAndEntrada(int page, Long sesionId, boolean entrada) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var sesion = sesionCajaDao.findByIdAndActiveTrue(sesionId);
        if (sesion.isEmpty()) {
            throw new BadRequestException("No existe sesion con ese Id");
        }
        var movimientoPage = dao.findAllBySesionAndEntradaAndActiveTrue(pageRequest,sesion.get(),entrada);
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
    public List<MovimientoDto> getAllBySesionAndEntrada(Long sesionId, boolean entrada) {
        var sesion = sesionCajaDao.findByIdAndActiveTrue(sesionId);
        if (sesion.isEmpty()) {
            throw new BadRequestException("No existe sesion con ese Id");
        }
        var movimientos = dao.findAllBySesionAndEntradaAndActiveTrue(sesion.get(),entrada);
        return movimientos.stream()
                .map(movimientoBean -> mapper.toDto(movimientoBean))
                .collect(Collectors.toList());
    }

}
