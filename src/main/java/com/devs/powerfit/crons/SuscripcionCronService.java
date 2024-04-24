package com.devs.powerfit.crons;

import com.devs.powerfit.dtos.suscripciones.SuscripcionDto;
import com.devs.powerfit.services.suscripciones.SuscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuscripcionCronService {

    private final SuscripcionService suscripcionService;
    @Autowired
    public SuscripcionCronService(SuscripcionService suscripcionService) {
        this.suscripcionService = suscripcionService;
    }

    @Scheduled(cron = "0 0 0 * * *") // Se ejecuta todos los días a la medianoche
    public void generarNuevasSuscripciones() {
        // Obtener las suscripciones existentes que ya están pagadas y cuya fecha de fin ha pasado
        List<SuscripcionDto> suscripcionesPagadas = suscripcionService.obtenerSuscripcionesPagadas().stream()
                .filter(suscripcion -> suscripcion.getFechaFin().isBefore( LocalDate.now())) // Filtrar por fecha de fin pasada
                .collect(Collectors.toList());

        // Procesar cada suscripción existente para generar nuevas suscripciones si es necesario
        for (SuscripcionDto suscripcionPagada : suscripcionesPagadas) {
            // Calcular la fecha de inicio y fin para la nueva suscripción
            LocalDate nuevaFechaInicio = suscripcionPagada.getFechaFin().plusDays(1); // Comienza un día después de la fecha de fin
            LocalDate nuevaFechaFin;
            if ("MENSUAL".equals(suscripcionPagada.getModalidad())) {
                nuevaFechaFin = nuevaFechaInicio.plusMonths(1); // Termina un mes después de la fecha de inicio
            } else {
                nuevaFechaFin = nuevaFechaInicio.plusWeeks(1); // Termina una semana después de la fecha de inicio
            }

            // Crear la nueva suscripción con las fechas actualizadas
            SuscripcionDto nuevaSuscripcion = new SuscripcionDto();
            nuevaSuscripcion.setClienteId(suscripcionPagada.getClienteId());
            nuevaSuscripcion.setActividadId(suscripcionPagada.getActividadId());
            nuevaSuscripcion.setModalidad(suscripcionPagada.getModalidad());
            nuevaSuscripcion.setEstado("PENDIENTE"); // Estado inicial pendiente
            nuevaSuscripcion.setFechaInicio(nuevaFechaInicio);
            nuevaSuscripcion.setFechaFin(nuevaFechaFin);

            // Guardar la nueva suscripción
            suscripcionService.create(nuevaSuscripcion);

            // Actualizar la suscripción antigua como finalizada
            suscripcionService.actualizarFinalizado(suscripcionPagada.getId());
        }
    }
}
