package com.devs.powerfit.services.arqueo;

import com.devs.powerfit.dtos.arqueo.ArqueoConDetallesDto;
import com.devs.powerfit.dtos.arqueo.ArqueoDetalleDto;
import com.devs.powerfit.dtos.arqueo.ArqueoDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArqueoConDetallesService {

    private ArqueoService arqueoService;
    private ArqueoDetalleService detalleService;

    public ArqueoConDetallesService(ArqueoService arqueoService, ArqueoDetalleService detalleService) {
        this.arqueoService = arqueoService;
        this.detalleService = detalleService;
    }

//    public ArqueoConDetallesDto generar(ArqueoConDetallesDto arqueoDto){
//        ArqueoDto arqueoCreado = arqueoService.generarArqueo(arqueoDto.getArqueo());
//
//        List<ArqueoDetalleDto> detallesCreados = arqueoDto.getDetalles().stream()
//                .map(detalle -> {
//                    detalle.setArqueoId(arqueoCreado.getId());
//                    return detalleService.crearArqueoDetalle(detalle);
//                }).collect(Collectors.toList());
//        ArqueoConDetallesDto newArqueo = new ArqueoConDetallesDto();
//        newArqueo.setArqueo(arqueoCreado);
//        newArqueo.setDetalles(detallesCreados);
//        return newArqueo;
//    }
}
