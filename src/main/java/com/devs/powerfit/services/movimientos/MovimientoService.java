package com.devs.powerfit.services.movimientos;

import com.devs.powerfit.beans.auth.UsuarioBean;
import com.devs.powerfit.beans.cajas.CajaBean;
import com.devs.powerfit.beans.cajas.SesionCajaBean;
import com.devs.powerfit.beans.movimientos.MovimientoBean;
import com.devs.powerfit.daos.auth.UsuarioDao;
import com.devs.powerfit.daos.cajas.CajaDao;
import com.devs.powerfit.daos.cajas.SesionCajaDao;
import com.devs.powerfit.daos.facturas.FacturaDao;
import com.devs.powerfit.daos.movimientos.MovimientoDao;
import com.devs.powerfit.dtos.facturas.FacturaDto;
import com.devs.powerfit.dtos.facturas.FacturaProveedorDto;
import com.devs.powerfit.dtos.movimientos.MovimientoDto;
import com.devs.powerfit.dtos.tickets.TicketDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.movimientos.IMovimientoService;
import com.devs.powerfit.services.cajas.SesionCajaService;
import com.devs.powerfit.services.facturas.FacturaProveedorService;
import com.devs.powerfit.services.facturas.FacturaService;
import com.devs.powerfit.services.tickets.TicketService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.facturaMappers.FacturaMapper;
import com.devs.powerfit.utils.mappers.facturaMappers.FacturaProveedorMapper;
import com.devs.powerfit.utils.mappers.movimientoMappers.MovimientoMapper;
import com.devs.powerfit.utils.mappers.ticketMappers.TicketMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovimientoService implements IMovimientoService {
    private final MovimientoDao dao;
    private final FacturaDao facturaDao;
    private final SesionCajaService sesionCajaService;
    private final FacturaService facturaService;
    private final FacturaMapper facturaMapper;
    private final SesionCajaDao sesionCajaDao;
    private final FacturaProveedorService facturaProveedorService;
    private final FacturaProveedorMapper facturaProveedorMapper;
    private final MovimientoMapper mapper;
    private final UsuarioDao usuarioDao;
    private final CajaDao cajaDao;
    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @Autowired
    public MovimientoService(MovimientoDao dao, FacturaDao facturaDao, SesionCajaService sesionCajaService, FacturaService facturaService, FacturaMapper facturaMapper, SesionCajaDao sesionCajaDao, FacturaProveedorService facturaProveedorService, FacturaProveedorMapper facturaProveedorMapper, MovimientoMapper mapper, UsuarioDao usuarioDao, CajaDao cajaDao, TicketService ticketService, TicketMapper ticketMapper) {
        this.dao = dao;
        this.facturaDao = facturaDao;
        this.sesionCajaService = sesionCajaService;
        this.facturaService = facturaService;
        this.facturaMapper = facturaMapper;
        this.sesionCajaDao = sesionCajaDao;
        this.facturaProveedorService = facturaProveedorService;
        this.facturaProveedorMapper = facturaProveedorMapper;
        this.mapper = mapper;
        this.usuarioDao = usuarioDao;
        this.cajaDao = cajaDao;
        this.ticketService = ticketService;
        this.ticketMapper = ticketMapper;
    }

    @Override
    public MovimientoDto create(MovimientoDto movimientoDto) {
        // Verificar que sesionId sea válido
        Optional<SesionCajaBean> sesion = sesionCajaDao.findByIdAndActiveTrue(movimientoDto.getSesionId());
        if (sesion.isEmpty()) {
            throw new BadRequestException("SesionId debe ser válido.");
        }
        Optional<CajaBean> caja = cajaDao.findByIdAndActiveTrue(sesion.get().getCaja().getId());
        if (caja.isEmpty()) {
            throw new NotFoundException("La caja no existe.");
        }
        String nombreCaja=obtenerNombreDeCaja(sesion.get().getId());
        String nombreEmpleado=obtenerNombreDeEmpleado(sesion.get().getId());


        // Generar la fecha actual si no se proporciona
        if (movimientoDto.getFecha() == null) {
            movimientoDto.setFecha(LocalDate.now());
        }

        // Generar la hora actual si no se proporciona
        if (movimientoDto.getHora() == null) {
            movimientoDto.setHora(LocalTime.now());
        }

        // Verificar si es una entrada o salida
        if (movimientoDto.isEntrada()) {
            if(movimientoDto.getFacturaId()!=null && movimientoDto.getTicketId()==null){
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
                movimiento.setNombreCaja(nombreCaja);
                movimiento.setNombreEmpleado(nombreEmpleado);
                MovimientoBean creado = dao.save(movimiento);
                return mapper.toDto(creado);
            } else if (movimientoDto.getTicketId() != null && movimientoDto.getFacturaId() == null) {
                TicketDto ticket = ticketService.getById(movimientoDto.getTicketId());
                if (ticket == null) {
                    throw new BadRequestException("La factura no existe");
                }
                if (ticket.isPagado()) {
                    throw new BadRequestException("La factura ya está pagada");
                }
                if (ticket.getSaldo() < movimientoDto.getTotal()) {
                    throw new BadRequestException("El saldo es menor al total del movimiento");
                }
                MovimientoBean movimiento = new MovimientoBean();
                movimiento.setActive(true);
                movimiento.setHora(movimientoDto.getHora());
                movimiento.setFecha(movimientoDto.getFecha());
                movimiento.setTotal(movimientoDto.getTotal());
                movimiento.setEntrada(movimientoDto.isEntrada());
                // Restar el total del movimiento al saldo de la factura
                ticket.setSaldo(ticket.getSaldo() - movimientoDto.getTotal());
                TicketDto ticketActualizado = ticketService.actualizarSaldo(ticket.getId(), ticket.getSaldo());
                if (ticket.getSaldo() == 0) {
                    ticketActualizado = ticketService.modificarPagado(ticket.getId(), true);
                }
                movimiento.setTicket(ticketMapper.toBean(ticketActualizado));
                movimiento.setSesion(sesion.get());
                if(!sesionCajaService.aumentarMontoCaja(movimiento.getSesion().getId(), movimiento.getTotal())){
                    throw new BadRequestException("Ha ocurrido un error al momento de actualizar el monto de caja");
                }
                movimiento.setFacturaProveedor(null);
                movimiento.setNombreCaja(nombreCaja);
                movimiento.setNombreEmpleado(nombreEmpleado);
                MovimientoBean creado = dao.save(movimiento);
                return mapper.toDto(creado);
            }
        } else {
            // Verificar que facturaProveedorId sea válido y facturaId sea nulo
            if (movimientoDto.getFacturaProveedorId() ==null ) {
                throw new BadRequestException("FacturaProveedorId debe ser válido y FacturaId debe ser nulo para una salida.");
            }
            if (caja.get().getMonto()< movimientoDto.getTotal()){
                throw new BadRequestException("El monto en caja es insuficiente para pagar la factura proveedor");
            }
            FacturaProveedorDto facturaProveedor = facturaProveedorService.getById(movimientoDto.getFacturaProveedorId());
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
            movimiento.setNombreCaja(nombreCaja);
            movimiento.setNombreEmpleado(nombreEmpleado);
            MovimientoBean creado = dao.save(movimiento);
            return mapper.toDto(creado);
        }
        throw new BadRequestException("Error al crear movimiento");
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
    public List<MovimientoDto> getByFacturaId(Long facturaId) {
        var factura = facturaDao.findByIdAndActiveTrue(facturaId);
        if (factura.isEmpty()) {
            throw new BadRequestException("No existe sesion con ese Id");
        }
        List<MovimientoBean> movimientoBeanList = dao.findAllByFacturaAndActiveTrue(factura.get());
        if (movimientoBeanList.isEmpty()) {
            throw new NotFoundException("No hay movimientos en la lista");
        }
        List<MovimientoDto> movimientoDtoList = movimientoBeanList.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return movimientoDtoList;
    }
    @Override
    public List<MovimientoDto> getAllBySesionId(Long sesionId) {
        var sesion = sesionCajaDao.findByIdAndActiveTrue(sesionId);
        if (sesion.isEmpty()) {
            throw new BadRequestException("No existe sesion con ese Id");
        }
        var movimientos = dao.findAllBySesionAndActiveTrue(sesion.get());
        return movimientos.stream()
                .map(mapper::toDto)
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
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
    private String obtenerNombreDeEmpleado(Long sesionId) {
        Optional<SesionCajaBean> sesionOptional = sesionCajaDao.findByIdAndActiveTrue(sesionId);
        if (sesionOptional.isPresent()) {
            SesionCajaBean sesionBean = sesionOptional.get();
            Optional<UsuarioBean> cajaOptional = usuarioDao.findByIdAndActiveTrue(sesionBean.getUsuario().getId());
            if (cajaOptional.isPresent()) {
                UsuarioBean usuarioBean = cajaOptional.get();
                return usuarioBean.getNombre();
            } else {
                throw new BadRequestException("La caja asociada a la sesión no fue encontrada.");
            }
        } else {
            throw new BadRequestException("La sesión no fue encontrada.");
        }
    }
    private String obtenerNombreDeCaja(Long sesionId) {
        Optional<SesionCajaBean> sesionOptional = sesionCajaDao.findByIdAndActiveTrue(sesionId);
        if (sesionOptional.isPresent()) {
            SesionCajaBean sesionBean = sesionOptional.get();
            Optional<CajaBean> cajaOptional = cajaDao.findByIdAndActiveTrue(sesionBean.getCaja().getId());
            if (cajaOptional.isPresent()) {
                CajaBean cajaBean = cajaOptional.get();
                return cajaBean.getNombre();
            } else {
                throw new BadRequestException("La caja asociada a la sesión no fue encontrada.");
            }
        } else {
            throw new BadRequestException("La sesión no fue encontrada.");
        }
    }

}
