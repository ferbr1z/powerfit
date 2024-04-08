package com.devs.powerfit.services.clientes;

import com.devs.powerfit.beans.clientes.ClienteBean;
import com.devs.powerfit.beans.suscripciones.SuscripcionBean;
import com.devs.powerfit.daos.clientes.ClienteDao;
import com.devs.powerfit.daos.suscripciones.SuscripcionDao;
import com.devs.powerfit.dtos.suscripciones.SuscripcionGananciasDto;
import com.devs.powerfit.dtos.suscripciones.SuscripcionesEstadisticasDto;
import com.devs.powerfit.enums.EEstado;
import com.devs.powerfit.exceptions.BadRequestException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Transactional
public class ReportesClienteService {
    private final ClienteDao clienteDao;
    private final SuscripcionDao suscripcionDao;
    @Autowired
    public ReportesClienteService(ClienteDao clienteDao, SuscripcionDao suscripcionDao) {
        this.clienteDao = clienteDao;
        this.suscripcionDao = suscripcionDao;
    }

    public SuscripcionesEstadisticasDto cantidadClientesPorEstadoSuscripcion() {
        List<ClienteBean> clientes = clienteDao.findAllByActiveTrue();

        Long clientesConSuscripcionPendiente = clientes.stream()
                .filter(cliente -> tieneSuscripcionPendiente(cliente))
                .count();

        Long clientesSinSuscripcion = clientes.stream()
                .filter(cliente -> !tieneSuscripcion(cliente))
                .count();

        Long clientesEnRegla = clientes.size() - clientesConSuscripcionPendiente - clientesSinSuscripcion;

        SuscripcionesEstadisticasDto suscripcionesEstadisticasDto = new SuscripcionesEstadisticasDto();
        suscripcionesEstadisticasDto.setCantidadClientesMorosos(clientesConSuscripcionPendiente);
        suscripcionesEstadisticasDto.setCantidadClientesEnRegla(clientesEnRegla);
        return suscripcionesEstadisticasDto;
    }

    private boolean tieneSuscripcion(ClienteBean cliente) {
        List<SuscripcionBean> suscripciones = suscripcionDao.findAllByClienteAndActiveTrue(cliente);
        return !suscripciones.isEmpty();
    }
    private boolean tieneSuscripcionPendiente(ClienteBean cliente) {
        List<SuscripcionBean> suscripciones = suscripcionDao.findAllByClienteAndActiveTrue(cliente);
        return suscripciones.stream()
                .anyMatch(suscripcion -> suscripcion.getEstado() == EEstado.PENDIENTE);
    }
    public SuscripcionGananciasDto calcularGanancias() {

        List<SuscripcionBean> suscripcionBeans = suscripcionDao.findAll();
        if(suscripcionBeans==null){
            throw new BadRequestException("No existen suscripciones activas");
        }
        Double gananciasPotenciales = suscripcionBeans.stream()
                .mapToDouble(SuscripcionBean::getMonto)
                .sum();

        Double gananciasPagadas = suscripcionBeans.stream()
                .filter(suscripcion -> suscripcion.getEstado() == EEstado.PAGADO)
                .mapToDouble(SuscripcionBean::getMonto)
                .sum();

        Double perdidasMorosos = suscripcionBeans.stream()
                .filter(suscripcion -> suscripcion.getEstado() == EEstado.PENDIENTE)
                .mapToDouble(SuscripcionBean::getMonto)
                .sum();

        SuscripcionGananciasDto dto=new SuscripcionGananciasDto();
        dto.setGananciasPotenciales(gananciasPotenciales);
        dto.setGananciaActual(gananciasPagadas);
        dto.setPerdidasMorosos(perdidasMorosos);
        return dto;
    }

}


