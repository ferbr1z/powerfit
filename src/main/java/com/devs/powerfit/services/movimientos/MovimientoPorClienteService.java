package com.devs.powerfit.services.movimientos;

import com.devs.powerfit.daos.clientes.ClienteDao;
import com.devs.powerfit.daos.facturas.FacturaDao;
import com.devs.powerfit.dtos.clientes.PagoClienteDto;
import com.devs.powerfit.dtos.movimientos.MovimientoDto;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovimientoPorClienteService {
    private final MovimientoService movimientoService;
    private final MovimientoDetalleService detalleService;
    private final ClienteDao clienteDao;
    private final FacturaDao facturaDao;

    @Autowired
    public MovimientoPorClienteService( MovimientoService movimientoService, MovimientoDetalleService detalleService, ClienteDao clienteDao, FacturaDao facturaDao) {
        this.movimientoService = movimientoService;
        this.detalleService = detalleService;
        this.clienteDao = clienteDao;
        this.facturaDao = facturaDao;
    }
    public PageResponse<PagoClienteDto> obtenerPagosPorCliente(Long clienteId, int page) {
        var clienteOptional = clienteDao.findByIdAndActiveTrue(clienteId);
        if (clienteOptional.isEmpty()) {
            throw new NotFoundException("No existe cliente con ese ID");
        }

        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var facturasClientePage = facturaDao.findAllByClienteAndPagadoTrueAndActiveTrue(pageRequest, clienteOptional.get());

        List<PagoClienteDto> pagos = facturasClientePage.stream()
                .map(factura -> {
                    PagoClienteDto pago = new PagoClienteDto();

                    List<MovimientoDto> movimientos = movimientoService.getByFacturaId(factura.getId());
                    Double total = movimientos.stream()
                            .mapToDouble(MovimientoDto::getTotal)
                            .sum();

                    // Obtener la fecha del primer movimiento
                    LocalDate fechaMovimiento = null;
                    if (!movimientos.isEmpty()) {
                        fechaMovimiento = movimientos.get(0).getFecha();
                    }

                    // Asignar el número de factura al objeto PagoClienteDto
                    pago.setNroFactura(factura.getNroFactura());

                    pago.setMonto(total);
                    pago.setFecha(fechaMovimiento);

                    return pago;
                })
                .collect(Collectors.toList());

        return new PageResponse<>(pagos, facturasClientePage.getTotalPages(), facturasClientePage.getTotalElements(), page);
    }




}
