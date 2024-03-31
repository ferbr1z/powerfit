package com.devs.powerfit.services.actividades;

import com.devs.powerfit.beans.actividades.ActividadBean;
import com.devs.powerfit.beans.empleados.EmpleadoBean;
import com.devs.powerfit.daos.actividades.ActividadDao;
import com.devs.powerfit.daos.empleados.EmpleadoDao;
import com.devs.powerfit.dtos.actividades.ActividadDto;
import com.devs.powerfit.dtos.empleados.EmpleadoDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.actividades.IActividadService;
import com.devs.powerfit.interfaces.empleados.IEmpleadoService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.actividadMapper.ActividadMapper;
import com.devs.powerfit.utils.mappers.empleadoMappers.EmpleadoMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ActividadService implements IActividadService  {

    private final ActividadDao actividadDao;
    private final ActividadMapper mapper;

    private final IEmpleadoService empleadoService;

    private final EmpleadoMapper empleadoMapper;

    private final EmpleadoDao empleadoDao;

    @Autowired
    public ActividadService(ActividadDao actividadDao, ActividadMapper mapper, IEmpleadoService empleadoService, EmpleadoMapper empleadoMapper, EmpleadoDao empleadoDao) {
        this.actividadDao = actividadDao;
        this.mapper = mapper;
        this.empleadoService = empleadoService;
        this.empleadoMapper = empleadoMapper;
        this.empleadoDao = empleadoDao;
    }







    @Override
    public ActividadDto create(ActividadDto actividadDto) {
        // Verificar si el nombre de la actividad está presente y otros campos obligatorios
        if (actividadDto.getNombre() == null || actividadDto.getNombre().isEmpty() || actividadDto.getCostoMensual() == null || actividadDto.getCostoSemanal() == null ){
            throw new BadRequestException("El nombre, costo mensual y semanal de la actividad no pueden estar vacíos.");
        }

        // Verificar si el costo mensual es válido (mayor o igual a 0)
        if (actividadDto.getCostoMensual() < 0) {
            throw new BadRequestException("El costo mensual de la actividad no puede ser negativo." );
        }

        // Verificar si el costo semanal es válido (mayor o igual a 0)
        if (actividadDto.getCostoSemanal() < 0)  {
            throw new BadRequestException("El costo semanal de la actividad no puede ser negativo.");
        }

        EmpleadoDto empleado = empleadoService.getById(actividadDto.getEntrenador());

        // Crear la actividad sin los entrenadores
        ActividadBean actividad = mapper.toBean(actividadDto);
        actividad.setActive(true);
        actividad.setEntrenador(empleadoMapper.toBean(empleado));


        // Guardar la actividad en la base de datos
        actividadDao.save(actividad);

        ActividadDto newActividad =new ActividadDto();
        newActividad.setId(actividad.getId());
        newActividad.setDescripcion(actividad.getDescripcion());
        newActividad.setCostoSemanal(actividad.getCostoSemanal());
        newActividad.setCostoMensual(actividad.getCostoMensual());
        newActividad.setNombre(actividad.getNombre());
        newActividad.setEntrenador(actividad.getEntrenador().getId());

        // Convertir la actividad guardada a DTO y retornarla
        return newActividad;
    }







    @Override
    public ActividadDto getById(Long id) {
        var actividad = actividadDao.findByIdAndActiveTrue(id);
        if (actividad.isPresent()) {
            return mapper.toDto(actividad.get());
        }
        throw new NotFoundException("actividad no encontrada");

    }

    @Override
    public PageResponse<ActividadDto> getAll(int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var actividades = actividadDao.findAllByActiveTrue(pag);
        if (actividades.isEmpty()) {
            throw new NotFoundException("No hay actividades en la lista");
        }
        var actividadesDto = actividades.map(mapper::toDto);
        return new PageResponse<>(actividadesDto.getContent(),
                actividadesDto.getTotalPages(),
                actividadesDto.getTotalElements(),
                actividadesDto.getNumber() + 1);
    }
    @Override
    public ActividadDto update(Long id, ActividadDto actividadDto) {
        // Verificar si el nombre de la actividad está presente
        if (actividadDto.getNombre() == null || actividadDto.getNombre().isEmpty() || actividadDto.getCostoMensual() == null || actividadDto.getCostoSemanal() == null ){
            throw new BadRequestException("El nombre, costo mensual y semanal de la actividad no pueden estar vacíos.");
        }

        // Verificar si el costo mensual es válido (mayor o igual a 0)
        if (actividadDto.getCostoMensual() < 0) {
            throw new BadRequestException("El costo mensual de la actividad no puede ser negativo.");
        }

        // Verificar si el costo semanal es válido (mayor o igual a 0)
        if (actividadDto.getCostoSemanal() < 0)  {
            throw new BadRequestException("El costo semanal de la actividad no puede ser negativo.");
        }
        var actividad = actividadDao.findByIdAndActiveTrue(id);
        if (actividad.isPresent()) {
            var actividadBean = actividad.get();
            EmpleadoDto empleadoDto = empleadoService.getById(actividadDto.getEntrenador());
            if (actividadDto.getNombre() != null) actividadBean.setNombre(actividadDto.getNombre());
            if (actividadDto.getCostoMensual() != null) actividadBean.setCostoMensual(actividadDto.getCostoMensual());
            if (actividadDto.getCostoSemanal() != null) actividadBean.setCostoSemanal(actividadDto.getCostoSemanal());
            if (actividadDto.getDescripcion() != null) actividadBean.setDescripcion(actividadDto.getDescripcion());
            if (actividadDto.getEntrenador() != null) actividadBean.setEntrenador(empleadoMapper.toBean(empleadoDto));
            actividadDao.save(actividadBean);

            ActividadDto newActividad =new ActividadDto();
            newActividad.setId(actividadBean.getId());
            newActividad.setActive(true);
            newActividad.setDescripcion(actividadBean.getDescripcion());
            newActividad.setCostoSemanal(actividadBean.getCostoSemanal());
            newActividad.setCostoMensual(actividadBean.getCostoMensual());
            newActividad.setNombre(actividadBean.getNombre());
            newActividad.setEntrenador(actividadBean.getEntrenador().getId());

            // Convertir la actividad guardada a DTO y retornarla
            return newActividad;
        }
        throw new NotFoundException("actividad no encontrada");
    }
    @Override
    public boolean delete(Long id) {
        var actividad = actividadDao.findByIdAndActiveTrue(id);
        if (actividad.isPresent()) {
            var actividadBean = actividad.get();
            actividadBean.setActive(false);
            actividadDao.save(actividadBean);
            return true;
        }
        throw new NotFoundException("actividad no encontrada");
    }
    @Override
    public PageResponse<ActividadDto> searchByNombre(String nombre, int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var actividades = actividadDao.findByNombreContainingIgnoreCaseAndActiveIsTrue(pag, nombre);

        if (actividades.isEmpty()) {
            throw new NotFoundException("No hay actividades en la lista");
        }

        var actividadesDto = actividades.map(mapper::toDto);
        return new PageResponse<>(
                actividadesDto.getContent(),
                actividadesDto.getTotalPages(),
                actividadesDto.getTotalElements(),
                actividadesDto.getNumber() + 1);
    }


}
