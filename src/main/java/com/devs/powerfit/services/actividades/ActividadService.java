package com.devs.powerfit.services.actividades;

import com.devs.powerfit.daos.actividades.ActividadDao;
import com.devs.powerfit.dtos.actividades.ActividadDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.actividades.IActividadService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.actividadMapper.ActividadMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ActividadService implements IActividadService  {
    private final ActividadDao actividadDao;
    private final ActividadMapper mapper;

    @Autowired
    public ActividadService(ActividadDao actividadDao, ActividadMapper mapper) {
        this.actividadDao = actividadDao;
        this.mapper = mapper;
    }

    @Override
    public ActividadDto create(ActividadDto actividadDto) {
        // Verificar si el nombre de la actividad está presente
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

        // Crear la actividad
        var actividad = mapper.toBean(actividadDto);
        actividad.setActive(true);
        actividadDao.save(actividad);

        return mapper.toDto(actividad);
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

            if (actividadDto.getNombre() != null) actividadBean.setNombre(actividadDto.getNombre());
            if (actividadDto.getCostoMensual() != null) actividadBean.setCostoMensual(actividadDto.getCostoMensual());
            if (actividadDto.getCostoSemanal() != null) actividadBean.setCostoSemanal(actividadDto.getCostoSemanal());
            if (actividadDto.getDescripcion() != null) actividadBean.setDescripcion(actividadDto.getDescripcion());
            actividadDao.save(actividadBean);

            return mapper.toDto(actividadBean);
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
