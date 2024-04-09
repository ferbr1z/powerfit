package com.devs.powerfit.services.movimientos;

import com.devs.powerfit.daos.clientes.ClienteDao;
import com.devs.powerfit.dtos.clientes.PagoClienteDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.services.facturas.FacturaService;
import com.devs.powerfit.utils.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MovimientoPorClienteService {
    private final FacturaService facturaService;
    private final MovimientoService movimientoService;
    private final MovimientoDetalleService detalleService;
    private final ClienteDao clienteDao;
    @Autowired
    public MovimientoPorClienteService(FacturaService facturaService, MovimientoService movimientoService, MovimientoDetalleService detalleService, ClienteDao clienteDao) {
        this.facturaService = facturaService;
        this.movimientoService = movimientoService;
        this.detalleService = detalleService;
        this.clienteDao = clienteDao;
    }
    public Page<PagoClienteDto>obtenerPagosPorCliente(Long clienteId,int page){
        var clienteOptional=clienteDao.findByIdAndActiveTrue(clienteId);
        if (clienteOptional.isEmpty()){
            throw new NotFoundException("No existe cliente con ese ID");
        }
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var facturasClientePage= facturaService.searchByNombreCliente()
    }
}
