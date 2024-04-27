package com.devs.powerfit.services.actividades;

import com.devs.powerfit.daos.actividades.ActividadDao;
import com.devs.powerfit.daos.clientes.ClienteDao;
import com.devs.powerfit.daos.suscripciones.SuscripcionDao;
import com.devs.powerfit.dtos.actividades.ActividadConClientesDto;
import com.devs.powerfit.dtos.actividades.ActividadDto;
import com.devs.powerfit.dtos.clientes.ClienteDto;
import com.devs.powerfit.dtos.suscripciones.SuscripcionConClienteDto;
import com.devs.powerfit.dtos.suscripciones.SuscripcionDto;
import com.devs.powerfit.enums.EModalidad;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.actividadMapper.ActividadMapper;
import com.devs.powerfit.utils.mappers.clienteMappers.ClienteMapper;
import com.devs.powerfit.utils.mappers.suscipcioneMapper.SuscripcionMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Service
@Transactional
public class ActividadConClientesService {
    private ActividadDao actividadDao;
    private SuscripcionDao suscripcionDao;
    private ClienteDao clienteDao;
    private ActividadMapper actividadMapper;
    private ClienteMapper clienteMapper;
    private SuscripcionMapper suscripcionMapper;

    public ActividadConClientesService(ActividadDao actividadDao, SuscripcionDao suscripcionDao, ClienteDao clienteDao, ActividadMapper actividadMapper, ClienteMapper clienteMapper, SuscripcionMapper suscripcionMapper) {
        this.actividadDao = actividadDao;
        this.suscripcionDao = suscripcionDao;
        this.clienteDao = clienteDao;
        this.actividadMapper = actividadMapper;
        this.clienteMapper = clienteMapper;
        this.suscripcionMapper = suscripcionMapper;
    }

    public PageResponse<ActividadConClientesDto> getAllActividadesConClientes(int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var actividades = actividadDao.findAllByActiveTrue(pag);
        if (actividades.isEmpty()) {
            throw new NotFoundException("No hay actividades en la lista");
        }

        List<ActividadConClientesDto> actividadesConClientes = actividades.stream()
                .map(actividad -> {
                    long clientesMensuales = suscripcionDao.countDistinctByActividadAndModalidadAndActiveTrue(actividad, EModalidad.MENSUAL);
                    long clientesSemanales = suscripcionDao.countDistinctByActividadAndModalidadAndActiveTrue(actividad, EModalidad.SEMANAL);
                    long totalClientes = clientesSemanales + clientesMensuales;

                    ActividadDto actividadDto = new ActividadDto();
                    actividadDto.setId(actividad.getId());
                    actividadDto.setNombre(actividad.getNombre());
                    actividadDto.setDescripcion(actividad.getDescripcion());
                    actividadDto.setCostoMensual(actividad.getCostoMensual());
                    actividadDto.setCostoSemanal(actividad.getCostoSemanal());

                    List<Long> entrenadoresIds = actividad.getEntrenadores().stream()
                            .map(entrenador -> entrenador.getId())
                            .collect(Collectors.toList());
                    actividadDto.setEntrenadores(entrenadoresIds);

                    return new ActividadConClientesDto(actividadDto, totalClientes);
                })
                .collect(Collectors.toList());

        return new PageResponse<>(actividadesConClientes,
                actividades.getTotalPages(),
                actividades.getTotalElements(),
                actividades.getNumber() + 1);
    }
    public PageResponse<SuscripcionConClienteDto> getSuscripcionesConClientesPorActividad(Long actividadId, int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE); // Ajusta Setting.PAGE_SIZE según tu configuración
        var actividad = actividadDao.findByIdAndActiveTrue(actividadId)
                .orElseThrow(() -> new NotFoundException("No se encontró la actividad con el ID proporcionado"));

        var suscripciones = suscripcionDao.findAllByActividadAndActiveTrue(actividad, pag);
        if (suscripciones.isEmpty()) {
            throw new NotFoundException("No hay suscripciones para la actividad con el ID proporcionado");
        }

        List<SuscripcionConClienteDto> suscripcionesConClientes = suscripciones.stream()
                .map(suscripcion -> {
                    ClienteDto clienteDto = clienteMapper.toDto(suscripcion.getCliente());
                    SuscripcionDto suscripcionDto = suscripcionMapper.toDto(suscripcion);
                    return new SuscripcionConClienteDto(suscripcionDto, clienteDto);
                })
                .collect(Collectors.toList());
        return new PageResponse<>(suscripcionesConClientes, suscripciones.getTotalPages(), suscripciones.getTotalElements(), page);
    }


}
