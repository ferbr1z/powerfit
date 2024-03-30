package com.devs.powerfit.crons;

import com.devs.powerfit.dtos.suscripciones.SuscripcionDto;
import com.devs.powerfit.services.suscripciones.SuscripcionService;
import com.devs.powerfit.utils.mappers.suscipcioneMapper.SuscripcionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class SuscripcionCronService {

    private final SuscripcionService suscripcionService;
    @Autowired
    public SuscripcionCronService(SuscripcionService suscripcionService) {
        this.suscripcionService = suscripcionService;
    }

    @Scheduled(cron = "0 0 0 * * *") // Se ejecuta todos los días a la medianoche
    public void generarNuevasSuscripciones() {
        // Obtener las suscripciones existentes que ya están pagadas
        List<SuscripcionDto> suscripcionesPagadas = suscripcionService.obtenerSuscripcionesPagadas();

        // Procesar cada suscripción existente para generar nuevas suscripciones si es necesario
        for (SuscripcionDto suscripcionPagada : suscripcionesPagadas) {
            // Calcular la fecha de inicio y fin para la nueva suscripción
            Date nuevaFechaInicio = sumarDias(suscripcionPagada.getFechaFin(), 1); // Comienza un día después de la fecha de fin
            Date nuevaFechaFin;
            if ("MENSUAL".equals(suscripcionPagada.getModalidad())) {
                nuevaFechaFin = sumarMeses(nuevaFechaInicio, 1); // Termina un mes después de la fecha de inicio
            } else {
                nuevaFechaFin = sumarSemanas(nuevaFechaInicio, 1); // Termina una semana después de la fecha de inicio
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


    // Método para sumar días a una fecha
    private Date sumarDias(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DAY_OF_MONTH, dias);
        return calendar.getTime();
    }

    // Método para sumar semanas a una fecha
    private Date sumarSemanas(Date fecha, int semanas) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.WEEK_OF_YEAR, semanas);
        return calendar.getTime();
    }

    // Método para sumar meses a una fecha
    private Date sumarMeses(Date fecha, int meses) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.MONTH, meses);
        return calendar.getTime();
    }
}
