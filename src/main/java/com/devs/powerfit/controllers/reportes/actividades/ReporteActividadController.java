package com.devs.powerfit.controllers.reportes.actividades;

import com.devs.powerfit.abstracts.AbstractReportesController;
import com.devs.powerfit.dtos.reportes.actividades.ActividadReportesDto;
import com.devs.powerfit.enums.EModalidad;
import com.devs.powerfit.interfaces.reportes.actividades.IActividadReportesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
public class ReporteActividadController extends AbstractReportesController {

    private final IActividadReportesService reporteActividadService;

    @Autowired
    public ReporteActividadController(IActividadReportesService reporteActividadService) {
        this.reporteActividadService = reporteActividadService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/actividades")
    ResponseEntity<List<ActividadReportesDto>> getActividades(){
        return new ResponseEntity<>(reporteActividadService.getActividadesConMasSuscripciones(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/suscripciones")
    ResponseEntity<List<ActividadReportesDto>> getActividadesSucripciones(){
        return new ResponseEntity<>(reporteActividadService.getActividadesMasRenovadas(), HttpStatus.OK);
    }


    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/suscripciones/{modalidad}")
    ResponseEntity<List<ActividadReportesDto>> getActividadesSucripcionesPorModalidad(@PathVariable EModalidad modalidad){
        return new ResponseEntity<>(reporteActividadService.getActividadesConMasSuscripcionesPorModalidad(modalidad), HttpStatus.OK);
    }


}
