package com.devs.powerfit.services.cajas;



import com.devs.powerfit.beans.cajas.ExtraccionDeCajaBean;
import com.devs.powerfit.beans.empleados.EmpleadoBean;
import com.devs.powerfit.daos.cajas.ExtraccionCajaDao;
import com.devs.powerfit.daos.empleados.EmpleadoDao;
import com.devs.powerfit.dtos.cajas.CajaDto;
import com.devs.powerfit.dtos.cajas.ExtraccionDeCajaDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.cajas.IExtraccionCajaService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.CajaMappers.CajaMapper;
import com.devs.powerfit.utils.mappers.CajaMappers.ExtracciónCajaMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class ExtraccionCajaService implements IExtraccionCajaService {

    private final ExtracciónCajaMapper mapper;
    private final ExtraccionCajaDao extraccionCajaDao;
    private  final EmpleadoDao empleadoDao;

    private final CajaMapper cajaMapper;

    private final CajaService cajaService;



    @Autowired
    public ExtraccionCajaService(ExtracciónCajaMapper mapper, ExtraccionCajaDao extraccionCajaDao, EmpleadoDao empleadoDao, CajaMapper cajaMapper, CajaService cajaService) {
        this.mapper = mapper;
        this.extraccionCajaDao = extraccionCajaDao;
        this.empleadoDao = empleadoDao;
        this.cajaMapper = cajaMapper;
        this.cajaService = cajaService;
    }




    @Override
    public ExtraccionDeCajaDto create(ExtraccionDeCajaDto extraccionDeCajaDto) {
        ExtraccionDeCajaBean extraccionDeCajaBean = mapper.toBean(extraccionDeCajaDto);
        extraccionDeCajaBean.setActive(true);
        CajaDto caja = cajaService.getById(extraccionDeCajaDto.getIdCaja());
        if (caja == null){
            throw new NotFoundException("No existe una caja con ese id");
        }
        Optional<EmpleadoBean> empleado = empleadoDao.findByIdAndActiveIsTrue(extraccionDeCajaDto.getIdUsuario());
        if (empleado.isEmpty()){
            throw new NotFoundException("No existe un empleado con ese id");
        }
        if (extraccionDeCajaDto.getMonto() > caja.getMonto()){
            throw new BadRequestException("El monto que desea retirar es mayor al disponible actualmente.");
        }
        if (extraccionDeCajaDto.getHora() == null){
            extraccionDeCajaBean.setHora(LocalTime.now());
        }
        if (extraccionDeCajaDto.getFecha() == null){
            extraccionDeCajaBean.setFecha(LocalDate.now());
        }
        caja.setMonto(caja.getMonto() - extraccionDeCajaDto.getMonto());
        extraccionDeCajaBean.setUsuario(empleado.get());
        extraccionDeCajaBean.setCaja(cajaMapper.toBean(caja));
        extraccionDeCajaBean.setNombreCaja(caja.getNombre());
        extraccionDeCajaBean.setNombreUsuario(empleado.get().getNombre());

        extraccionCajaDao.save(extraccionDeCajaBean);

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
