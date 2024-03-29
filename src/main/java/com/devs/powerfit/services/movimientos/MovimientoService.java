package com.devs.powerfit.services.movimientos;

import com.devs.powerfit.beans.cajas.SesionCajaBean;
import com.devs.powerfit.beans.movimientos.MovimientoBean;
import com.devs.powerfit.daos.cajas.SesionCajaDao;
import com.devs.powerfit.daos.movimientos.MovimientoDao;
import com.devs.powerfit.dtos.cajas.SesionCajaDto;
import com.devs.powerfit.dtos.facturas.FacturaDto;
import com.devs.powerfit.dtos.facturas.FacturaProveedorDto;
import com.devs.powerfit.dtos.movimientos.MovimientoDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.interfaces.movimientos.IMovimientoService;
import com.devs.powerfit.services.cajas.SesionCajaService;
import com.devs.powerfit.services.facturas.FacturaProveedorService;
import com.devs.powerfit.services.facturas.FacturaService;
import com.devs.powerfit.utils.mappers.CajaMappers.SesionCajaMapper;
import com.devs.powerfit.utils.mappers.facturaMappers.FacturaMapper;
import com.devs.powerfit.utils.mappers.facturaMappers.FacturaProveedorMapper;
import com.devs.powerfit.utils.mappers.movimientoMappers.MovimientoMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MovimientoService implements IMovimientoService {
    private final MovimientoDao dao;
    private final FacturaService facturaService;
    private final FacturaMapper facturaMapper;
    private SesionCajaDao sesionCajaDao;
    private final FacturaProveedorService facturaProveedorService;
    private final FacturaProveedorMapper facturaProveedorMapper;
    private final SesionCajaMapper sesionCajaMapper;
    private final MovimientoMapper mapper;

    @Autowired
    public MovimientoService(MovimientoDao dao, FacturaService facturaService, FacturaMapper facturaMapper, SesionCajaDao sesionCajaDao, FacturaProveedorService facturaProveedorService, FacturaProveedorMapper facturaProveedorMapper, SesionCajaMapper sesionCajaMapper, MovimientoMapper mapper) {
        this.dao = dao;
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
            MovimientoBean movimiento=new MovimientoBean();
            movimiento.setActive(true);
            movimiento.setHora(movimientoDto.getHora());
            movimiento.setFecha(movimientoDto.getFecha());
            movimiento.setTotal(movimientoDto.getTotal());
            movimiento.setEntrada(movimientoDto.isEntrada());
            // Restar el total del movimiento al saldo de la factura
            factura.setSaldo(factura.getSaldo() - movimientoDto.getTotal());
            FacturaDto facturaActualizada= facturaService.actualizarSaldo(factura.getId(),factura.getSaldo());
            if (factura.getSaldo() == 0) {
                facturaActualizada= facturaService.modificarPagado(factura.getId(),true);
            }
            movimiento.setFactura(facturaMapper.toBean(facturaActualizada));
            movimiento.setSesion(sesion.get());
            movimiento.setFacturaProveedor(null);
            MovimientoBean creado= dao.save(movimiento);
            return mapper.toDto(creado);

        } else {
            System.out.println("Salida");
            // Verificar que facturaProveedorId sea válido y facturaId sea nulo
            if (movimientoDto.getFacturaProveedorId() ==null ) {
                throw new BadRequestException("FacturaProveedorId debe ser válido y FacturaId debe ser nulo para una salida.");
            }
            System.out.println("Verifica");
            FacturaProveedorDto facturaProveedor = facturaProveedorService.getById(movimientoDto.getFacturaProveedorId());
            System.out.println(facturaProveedor);
            if (facturaProveedor == null) {
                throw new BadRequestException("La factura del proveedor no existe");
            }
            System.out.println("Verifica la factura proveedor");
            if (facturaProveedor.getSaldo() < movimientoDto.getTotal()) {
                throw new BadRequestException("El saldo es menor al total del movimiento");
            }
            MovimientoBean movimiento=new MovimientoBean();
            System.out.println("Crea bean");
            movimiento.setActive(true);
            System.out.println("active");
            movimiento.setHora(movimientoDto.getHora());
            System.out.println("hora");
            movimiento.setFecha(movimientoDto.getFecha());
            System.out.println("fecha");
            movimiento.setTotal(movimientoDto.getTotal());
            System.out.println("total");
            movimiento.setEntrada(movimientoDto.isEntrada());
            System.out.println("boolean");
            // Restar el total del movimiento al saldo de la factura del proveedor
            facturaProveedor.setSaldo(facturaProveedor.getSaldo() - movimientoDto.getTotal());
            System.out.println("resta saldo");
            if(facturaProveedor.getSaldo()==0){
                facturaProveedorService.modificarPagado(facturaProveedor.getId(),true);
                System.out.println("pagado");
            }
            System.out.println(facturaProveedor);
            FacturaProveedorDto facturaProveedorActualizada = facturaProveedorService.actualizarSaldo(facturaProveedor.getId(),facturaProveedor.getSaldo());
            System.out.println("actualizado");
            movimiento.setFacturaProveedor(facturaProveedorMapper.toBean(facturaProveedorActualizada));
            System.out.println("Actualiza factura proveedor");
            movimiento.setSesion(sesion.get());
            System.out.println("sesion");
            movimiento.setFactura(null);
            System.out.println("factura null");
            MovimientoBean creado = dao.save(movimiento);
            System.out.println("guarda");
            return mapper.toDto(creado);
        }
    }



    @Override
    public MovimientoDto getById(Long id) {
        return null;
    }

    @Override
    public PageResponse<MovimientoDto> getAll(int page) {
        return null;
    }

    @Override
    public MovimientoDto update(Long id, MovimientoDto movimientoDto) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public PageResponse<MovimientoDto> searchBySesionId(Long sesionId, int page) {
        return null;
    }

    @Override
    public List<MovimientoDto> getAllBySesionId(Long sesionId) {
        return null;
    }




    @Override
    public PageResponse<MovimientoDto> searchBySesion(int page, Date fechaMenor, Date fechaMayor) {
        return null;
    }

    @Override
    public PageResponse<MovimientoDto> searchBySesionAndEntrada(int page, Long sesionId, boolean entrada) {
        return null;
    }

    @Override
    public List<MovimientoDto> getAllBySesionAndEntrada(Long sesionId, boolean entrada) {
        return null;
    }

    @Override
    public PageResponse<MovimientoDto> searchByFactura(int page, FacturaDto factura) {
        return null;
    }

    @Override
    public List<MovimientoDto> getAllByFactura(FacturaDto factura) {
        return null;
    }

    @Override
    public PageResponse<MovimientoDto> searchByFacturaProveedor(int page, FacturaProveedorDto facturaProveedor) {
        return null;
    }

    @Override
    public List<MovimientoDto> getAllByFacturaProveedor(FacturaProveedorDto facturaProveedor) {
        return null;
    }
}
