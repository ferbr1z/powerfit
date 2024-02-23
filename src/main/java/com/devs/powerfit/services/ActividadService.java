package com.devs.powerfit.services;

import com.devs.powerfit.daos.ActividadDao;
import com.devs.powerfit.dtos.ActividadDto;
import com.devs.powerfit.dtos.PageResponse;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.IActividadService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.actividadMapper.ActividadMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
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
        var actividades = actividadDao.findByNombreAndActiveIsTrue(pag, nombre);

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
