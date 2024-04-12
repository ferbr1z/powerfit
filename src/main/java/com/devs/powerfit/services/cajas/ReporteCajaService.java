package com.devs.powerfit.services.cajas;

import com.devs.powerfit.beans.cajas.CajaBean;
import com.devs.powerfit.daos.cajas.CajaDao;
import com.devs.powerfit.dtos.cajas.CajaDto;
import com.devs.powerfit.dtos.cajas.TotalCajasDto;
import com.devs.powerfit.utils.mappers.CajaMappers.CajaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteCajaService {
    private final CajaDao dao;
    private final CajaMapper mapper;
    @Autowired
    public ReporteCajaService(CajaDao dao, CajaMapper mapper) {
        this.dao = dao;
        this.mapper = mapper;
    }

    public TotalCajasDto obtenerTotalDeCajas() {
        // Obtener todas las cajas de la base de datos
        List<CajaBean> cajas = dao.findAllByActiveTrue();

        double total = cajas.stream()
                .mapToDouble(CajaBean::getMonto)
                .sum();

        List<CajaDto> cajasDto = cajas.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        TotalCajasDto totalCajasDto = new TotalCajasDto();
        totalCajasDto.setCajas(cajasDto);
        totalCajasDto.setTotal(total);
        return totalCajasDto;
    }
}
