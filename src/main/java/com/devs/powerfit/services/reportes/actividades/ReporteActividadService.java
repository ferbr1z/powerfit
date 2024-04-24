package com.devs.powerfit.services.reportes.actividades;

import com.devs.powerfit.beans.suscripciones.SuscripcionBean;
import com.devs.powerfit.daos.suscripciones.SuscripcionDao;
import com.devs.powerfit.dtos.reportes.actividades.ActividadReportesDto;
import com.devs.powerfit.enums.EEstado;
import com.devs.powerfit.enums.EModalidad;
import com.devs.powerfit.interfaces.reportes.actividades.IActividadReportesService;
import com.devs.powerfit.services.actividades.ActividadService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class ReporteActividadService implements IActividadReportesService {

    private final SuscripcionDao suscripcionDao;
    private final ActividadService actividadService;

    @Autowired
    public ReporteActividadService(SuscripcionDao suscripcionDao, ActividadService actividadService) {
        this.suscripcionDao = suscripcionDao;
        this.actividadService = actividadService;
    }
    @Override
    public List<ActividadReportesDto> getActividadesConMasSuscripciones() {
        List<SuscripcionBean> suscripciones = suscripcionDao.findAllByActiveTrue();

        // Agrupar las suscripciones por actividad y contar la cantidad de suscripciones por actividad
        Map<String, Long> suscripcionesPorActividad = suscripciones.stream()
                .collect(Collectors.groupingBy(
                        suscripcion -> actividadService.getById(suscripcion.getActividad().getId()).getNombre(),
                        Collectors.counting()
                ));

        // Ordenar el mapa por valor (cantidad de suscripciones) en orden descendente
        return suscripcionesPorActividad.entrySet().stream()
                .map(entry -> new ActividadReportesDto(entry.getKey(), entry.getValue().intValue()))
                .sorted(Comparator.comparingInt(ActividadReportesDto::getCantidad).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    @Override
    public List<ActividadReportesDto> getActividadesMasRenovadas() {
        List<SuscripcionBean> suscripcionesActivas = suscripcionDao.findAllByActiveTrueAndFinalizadoFalse();

        return suscripcionesActivas.stream()
                .filter(suscripcion -> suscripcion.getEstado() == EEstado.PAGADO) // Filtrar por estado PAGADO
                .collect(Collectors.groupingBy(
                        suscripcion -> actividadService.getById(suscripcion.getActividad().getId()).getNombre(),
                        Collectors.groupingBy(SuscripcionBean::getActividad, Collectors.counting())
                ))
                .entrySet().stream() // Convertir el mapa a un stream de entradas
                .map(entry -> new ActividadReportesDto(entry.getKey(), entry.getValue().values().stream()
                        .max(Long::compare) // Encontrar el máximo (mayor número de renovaciones)
                        .orElse(0L).intValue())) // Valor predeterminado si no hay renovaciones
                .sorted(Comparator.comparingInt(ActividadReportesDto::getCantidad).reversed()) // Ordenar por cantidad en orden descendente
                .limit(3) // Tomar los primeros 3 elementos
                .collect(Collectors.toList());
    }

    @Override
    public List<ActividadReportesDto> getActividadesConMasSuscripcionesPorModalidad(EModalidad estado) {
        List<SuscripcionBean> suscripciones = suscripcionDao.findAllByActiveTrueAndModalidad(estado);

        // Agrupar las suscripciones por actividad y contar la cantidad de suscripciones por actividad
        Map<String, Long> suscripcionesPorActividad = suscripciones.stream()
                .collect(Collectors.groupingBy(
                        suscripcion -> actividadService.getById(suscripcion.getActividad().getId()).getNombre(),
                        Collectors.counting()
                ));

        // Ordenar el mapa por valor (cantidad de suscripciones) en orden descendente
        return suscripcionesPorActividad.entrySet().stream()
                .map(entry -> new ActividadReportesDto(entry.getKey(), entry.getValue().intValue()))
                .sorted(Comparator.comparingInt(ActividadReportesDto::getCantidad).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }


}
