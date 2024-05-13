package com.devs.powerfit.services.cajas;



import com.devs.powerfit.beans.cajas.ExtraccionDeCajaBean;
import com.devs.powerfit.daos.cajas.ExtraccionCajaDao;
import com.devs.powerfit.daos.cajas.SesionCajaDao;
import com.devs.powerfit.dtos.cajas.CajaDto;
import com.devs.powerfit.dtos.cajas.ExtraccionDeCajaDto;
import com.devs.powerfit.dtos.empleados.EmpleadoDto;
import com.devs.powerfit.dtos.movimientos.MovimientoConDetalleDto;
import com.devs.powerfit.dtos.movimientos.MovimientoDetalleDto;
import com.devs.powerfit.dtos.movimientos.MovimientoDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.cajas.ICajaService;
import com.devs.powerfit.interfaces.cajas.IExtraccionCajaService;
import com.devs.powerfit.interfaces.empleados.IEmpleadoService;
import com.devs.powerfit.services.movimientos.MovimientoConDetalleService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.CajaMappers.CajaMapper;
import com.devs.powerfit.utils.mappers.CajaMappers.ExtraccionCajaMapper;
import com.devs.powerfit.utils.mappers.empleadoMappers.EmpleadoMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ExtraccionCajaService implements IExtraccionCajaService {

    private final ExtraccionCajaMapper mapper;
    private final ExtraccionCajaDao extraccionCajaDao;
    private final CajaMapper cajaMapper;
    private final ICajaService cajaService;
    private final IEmpleadoService empleadoService;
    private final EmpleadoMapper empleadoMapper;

    private final MovimientoConDetalleService movimientoConDetalleService;
    private final SesionCajaDao sesionCajaDao;


    @Autowired
    public ExtraccionCajaService(SesionCajaDao sesionCajaDao, ExtraccionCajaMapper mapper, ExtraccionCajaDao extraccionCajaDao, CajaMapper cajaMapper, ICajaService cajaService, IEmpleadoService empleadoService, EmpleadoMapper empleadoMapper, MovimientoConDetalleService movimientoService) {
        this.mapper = mapper;
        this.extraccionCajaDao = extraccionCajaDao;
        this.cajaMapper = cajaMapper;
        this.cajaService = cajaService;
        this.empleadoService = empleadoService;
        this.empleadoMapper = empleadoMapper;
        this.movimientoConDetalleService = movimientoService;
        this.sesionCajaDao = sesionCajaDao;
    }




    @Override
    public ExtraccionDeCajaDto create(ExtraccionDeCajaDto extraccionDeCajaDto) {
        ExtraccionDeCajaBean extraccionDeCajaBean = mapper.toBean(extraccionDeCajaDto);
        extraccionDeCajaBean.setActive(true);
        CajaDto caja = cajaService.getById(extraccionDeCajaDto.getIdCaja());
        EmpleadoDto empleado = empleadoService.getById(extraccionDeCajaDto.getIdUsuario());
        if (extraccionDeCajaDto.getMonto() > caja.getMonto()){
            throw new BadRequestException("El monto que desea retirar es mayor al disponible actualmente.");
        }
        if (extraccionDeCajaDto.getHora() == null){
            extraccionDeCajaBean.setHora(LocalTime.now());
        }
        if (extraccionDeCajaDto.getFecha() == null){
            extraccionDeCajaBean.setFecha(LocalDate.now());
        }
        if (sesionCajaDao.findByIdAndActiveTrue(extraccionDeCajaDto.getSesionId()).isEmpty()){
            throw new NotFoundException("No existe una sesión con el ID proporcionado.");
        }
        caja.setMonto(caja.getMonto() - extraccionDeCajaDto.getMonto());
        cajaService.update(caja.getId(), caja);
        extraccionDeCajaBean.setUsuario(empleadoMapper.toBean(empleado));
        extraccionDeCajaBean.setCaja(cajaMapper.toBean(caja));
        extraccionDeCajaBean.setNombreCaja(caja.getNombre());
        extraccionDeCajaBean.setNombreUsuario(empleado.getNombre());
        extraccionCajaDao.save(extraccionDeCajaBean);

        MovimientoDto movimientoDto = new MovimientoDto();
        movimientoDto.setHora(LocalTime.now());
        movimientoDto.setFecha(LocalDate.now());
        movimientoDto.setTotal(extraccionDeCajaDto.getMonto());
        movimientoDto.setEntrada(false);
        movimientoDto.setFacturaId(null);
        movimientoDto.setSesionId(extraccionDeCajaDto.getSesionId());
        movimientoDto.setFacturaProveedorId(null);
        movimientoDto.setNombreCaja(caja.getNombre());
        movimientoDto.setNombreEmpleado(empleado.getNombre());
        movimientoDto.setExtraccionId(extraccionDeCajaBean.getId());

        MovimientoConDetalleDto movimientoConDetalleDto = new MovimientoConDetalleDto();
        movimientoConDetalleDto.setMovimiento(movimientoDto);

        MovimientoDetalleDto movimientoDetalleDto = new MovimientoDetalleDto();
        movimientoDetalleDto.setMonto(extraccionDeCajaDto.getMonto());
        movimientoDetalleDto.setTipoDePagoId(1L);
        List<MovimientoDetalleDto> detalles = List.of(movimientoDetalleDto);
        movimientoConDetalleDto.setDetalles(detalles);

        movimientoConDetalleService.create(movimientoConDetalleDto);

        return mapper.toDto(extraccionDeCajaBean);
    }

    @Override
    public ExtraccionDeCajaDto getById(Long id) {
        Optional<ExtraccionDeCajaBean> bean = extraccionCajaDao.findByIdAndActiveTrue(id);
        if (bean.isPresent()){
            return mapper.toDto(bean.get());
        }
        throw new NotFoundException("No existe una extracción con el ID proporcionado");
    }

    @Override
    public PageResponse<ExtraccionDeCajaDto> getAll(int page) {
        var pageable = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var extracciones = extraccionCajaDao.findAllByActiveIsTrue(pageable);
        if(extracciones.isEmpty()){
            throw new NotFoundException("No hay extracciones en la lista");
        }
        var extraccionesDto = extracciones.map(mapper::toDto);

        return new PageResponse<>(extraccionesDto.getContent(),
                extraccionesDto.getTotalPages(),
                extraccionesDto.getTotalElements(),
                extraccionesDto.getNumber() + 1);
    }

    //No se implementa
    @Override
    public ExtraccionDeCajaDto update(Long id, ExtraccionDeCajaDto extraccionDeCajaDto) {
        return null;
    }

    //No se implementa
    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public PageResponse<ExtraccionDeCajaDto> getAllBetween(int page, LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio.isAfter(fechaFin)) {
            throw new BadRequestException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        var pageable = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var extracciones = extraccionCajaDao.findAllByActiveIsTrueAndFechaBetween(pageable, fechaInicio, fechaFin);
        if(extracciones.isEmpty()){
            throw new NotFoundException("No hay extracciones en la lista");
        }
        var extraccionesDto = extracciones.map(mapper::toDto);

        return new PageResponse<>(extraccionesDto.getContent(),
                extraccionesDto.getTotalPages(),
                extraccionesDto.getTotalElements(),
                extraccionesDto.getNumber() + 1);
    }
}
