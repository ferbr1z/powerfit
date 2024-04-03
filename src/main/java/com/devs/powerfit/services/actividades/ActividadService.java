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
        // Crear la actividad
        var actividad = mapper.toBean(actividadDto); // Convertir el DTO a bean

        // Obtener la lista de entrenadores a partir de IDs y mapearlos a beans
        List<EmpleadoBean> entrenadores = actividadDto.getEntrenadores().stream()
                .map(empleadoService::getById)
                .map(empleadoMapper::toBean)
                .collect(Collectors.toList());

        actividad.setEntrenadores(entrenadores); // Establecer los entrenadores en la actividad
        actividad.setActive(true); // Establecer el estado activo

        actividadDao.save(actividad); // Guardar la actividad en la base de datos

        // Convertir la actividad bean a DTO y devolverla
        return getActividadDto(actividad, entrenadores);
    }



    @Override
    public ActividadDto getById(Long id) {
        var actividadOptional = actividadDao.findByIdAndActiveTrue(id);
        if (actividadOptional.isPresent()) {
            ActividadBean actividad = actividadOptional.get();
            ActividadDto actividadDto = new ActividadDto();
            actividadDto.setId(actividad.getId());
            actividadDto.setNombre(actividad.getNombre());
            actividadDto.setDescripcion(actividad.getDescripcion());
            actividadDto.setCostoMensual(actividad.getCostoMensual());
            actividadDto.setCostoSemanal(actividad.getCostoSemanal());

            List<Long> entrenadoresIds = actividad.getEntrenadores().stream()
                    .map(EmpleadoBean::getId)
                    .collect(Collectors.toList());
            actividadDto.setEntrenadores(entrenadoresIds);

            return actividadDto;
        } else {
            throw new NotFoundException("Actividad no encontrada");
        }
    }

    @Override
    public PageResponse<ActividadDto> getAll(int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var actividades = actividadDao.findAllByActiveTrue(pag);
        if (actividades.isEmpty()) {
            throw new NotFoundException("No hay actividades en la lista");
        }

        List<ActividadDto> actividadesDto = new ArrayList<>();
        for (ActividadBean actividad : actividades) {
            ActividadDto actividadDto = new ActividadDto();
            actividadDto.setId(actividad.getId());
            actividadDto.setNombre(actividad.getNombre());
            actividadDto.setDescripcion(actividad.getDescripcion());
            actividadDto.setCostoMensual(actividad.getCostoMensual());
            actividadDto.setCostoSemanal(actividad.getCostoSemanal());

            List<Long> entrenadoresIds = actividad.getEntrenadores().stream()
                    .map(EmpleadoBean::getId)
                    .collect(Collectors.toList());
            actividadDto.setEntrenadores(entrenadoresIds);

            actividadesDto.add(actividadDto);
        }

        return new PageResponse<>(actividadesDto,
                actividades.getTotalPages(),
                actividades.getTotalElements(),
                actividades.getNumber() + 1);
    }


    @Override
    public ActividadDto update(Long id, ActividadDto actividadDto) {
        // Verificar si el costo mensual es válido (mayor o igual a 0)
        if (actividadDto.getCostoMensual() < 0) {
            throw new BadRequestException("El costo mensual de la actividad no puede ser negativo." );
        }

        // Verificar si el costo semanal es válido (mayor o igual a 0)
        if (actividadDto.getCostoSemanal() < 0)  {
            throw new BadRequestException("El costo semanal de la actividad no puede ser negativo.");
        }

        var actividadOptional = actividadDao.findByIdAndActiveTrue(id);
        if (actividadOptional.isPresent()) {
            ActividadBean actividad = actividadOptional.get();

            // Verificar y actualizar los campos proporcionados en el DTO
            if (actividadDto.getNombre() != null) {
                actividad.setNombre(actividadDto.getNombre());
            }
            if (actividadDto.getDescripcion() != null) {
                actividad.setDescripcion(actividadDto.getDescripcion());
            }
            if (actividadDto.getCostoMensual() != null) {
                actividad.setCostoMensual(actividadDto.getCostoMensual());
            }
            if (actividadDto.getCostoSemanal() != null) {
                actividad.setCostoSemanal(actividadDto.getCostoSemanal());
            }
            List<EmpleadoBean> entrenadores = new ArrayList<>();
            // Actualizar la lista de entrenadores si se proporciona en el DTO
            if (actividadDto.getEntrenadores() != null) {
                for (Long entrenadorId : actividadDto.getEntrenadores()) {
                    EmpleadoBean entrenador = empleadoDao.findByIdAndActiveIsTrue(entrenadorId)
                            .orElseThrow(() -> new NotFoundException("Entrenador con ID " + entrenadorId + " no encontrado"));
                    entrenadores.add(entrenador);
                }
                actividad.setEntrenadores(entrenadores);
            }

            // Guardar los cambios en la base de datos
            actividadDao.save(actividad);

            return getActividadDto(actividad,entrenadores);
        } else {
            throw new NotFoundException("Actividad no encontrada");
        }
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

        List<ActividadDto> actividadesDto = new ArrayList<>();
        for (ActividadBean actividad : actividades) {
            ActividadDto actividadDto = new ActividadDto();
            actividadDto.setId(actividad.getId());
            actividadDto.setNombre(actividad.getNombre());
            actividadDto.setDescripcion(actividad.getDescripcion());
            actividadDto.setCostoMensual(actividad.getCostoMensual());
            actividadDto.setCostoSemanal(actividad.getCostoSemanal());

            List<Long> entrenadoresIds = actividad.getEntrenadores().stream()
                    .map(EmpleadoBean::getId)
                    .collect(Collectors.toList());
            actividadDto.setEntrenadores(entrenadoresIds);

            actividadesDto.add(actividadDto);
        }

        return new PageResponse<>(actividadesDto,
                actividades.getTotalPages(),
                actividades.getTotalElements(),
                actividades.getNumber() + 1);
    }


    private static ActividadDto getActividadDto(ActividadBean actividad, List<EmpleadoBean> entredadores) {
        ActividadDto newActivididad = new ActividadDto();
        newActivididad.setId(actividad.getId());
        newActivididad.setNombre(actividad.getNombre());
        newActivididad.setDescripcion(actividad.getDescripcion());
        newActivididad.setCostoMensual(actividad.getCostoMensual());
        newActivididad.setCostoSemanal(actividad.getCostoSemanal());
        newActivididad.setActive(true);
        List<Long> entrenadoresIds = new ArrayList<>();
        for (EmpleadoBean entrenador: entredadores){
            entrenadoresIds.add(entrenador.getId());
        }
        newActivididad.setEntrenadores(entrenadoresIds);
        return newActivididad;
    }


}
