package com.devs.powerfit.services.facturas;

import com.devs.powerfit.dtos.facturas.*;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.interfaces.facturas.IFacturaProveedorConDetallesService;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FacturaProveedorConDetallesService implements IFacturaProveedorConDetallesService {
    private final FacturaProveedorService facturaProveedorService;
    private final FacturaProveedorDetalleService facturaProveedorDetalleService;
    @Autowired
    public FacturaProveedorConDetallesService(FacturaProveedorService facturaProveedorService, FacturaProveedorDetalleService facturaProveedorDetalleService) {
        this.facturaProveedorService = facturaProveedorService;
        this.facturaProveedorDetalleService = facturaProveedorDetalleService;
    }

    @Override
    public FacturaProveedorConDetallesDto create(FacturaProveedorConDetallesDto facturaProveedorConDetallesDto) {
        if (facturaProveedorConDetallesDto.getDetalles() == null || facturaProveedorConDetallesDto.getDetalles().isEmpty()) {
            // Puedes manejar esta situación de acuerdo a tus requerimientos, como lanzar una excepción, enviar un mensaje de error, etc.
            // Por ejemplo, lanzar una excepción:
            throw new BadRequestException("La lista de detalles de la factura está vacía.");
        }
        // Crear la factura principal
        FacturaProveedorDto facturaCreada = facturaProveedorService.create(facturaProveedorConDetallesDto.getFactura());

        // Crear los detalles de la factura
        List<FacturaProveedorDetalleDto> detallesCreados = facturaProveedorConDetallesDto.getDetalles().stream()
                .map(detalle -> {
                    detalle.setFacturaId(facturaCreada.getId()); // Asignar el ID de la factura principal al detalle
                    return facturaProveedorDetalleService.create(detalle);
                })
                .collect(Collectors.toList());
        FacturaProveedorConDetallesDto nuevo = new FacturaProveedorConDetallesDto();
        nuevo.setFactura(facturaCreada);
        nuevo.setDetalles(detallesCreados);
        return nuevo;
    }

    @Override
    public FacturaProveedorConDetallesDto getById(Long id) {
        // Obtener la factura principal por su ID
        FacturaProveedorDto facturaDto = facturaProveedorService.getById(id);
        // Obtener los detalles de la factura por el ID de la factura principal
        List<FacturaProveedorDetalleDto> detallesDto = facturaProveedorDetalleService.getAllByFacturaId(id);
        FacturaProveedorConDetallesDto nuevo = new FacturaProveedorConDetallesDto();
        nuevo.setFactura(facturaDto);
        nuevo.setDetalles(detallesDto);
        // Devolver la factura con los detalles
        return nuevo;
    }

    @Override
    public PageResponse<FacturaProveedorConDetallesDto> getAll(int page) {
        // Obtener todas las facturas principales
        PageResponse<FacturaProveedorDto> facturaPage = facturaProveedorService.getAll(page);

        // Para cada factura, obtener sus detalles
        List<FacturaProveedorConDetallesDto> facturaConDetallesList = facturaPage.getItems().stream()
                .map(facturaDto -> getById(facturaDto.getId())) // Obtener la factura con detalles
                .collect(Collectors.toList());

        // Devolver la lista de facturas con detalles
        return new PageResponse<>(facturaConDetallesList, facturaPage.getTotalPages(), facturaPage.getTotalItems(), facturaPage.getCurrentPage());
    }

    @Override
    //No se usara
    public FacturaProveedorConDetallesDto update(Long id, FacturaProveedorConDetallesDto facturaProveedorConDetallesDto) {
        // Actualizar la factura principal
        FacturaProveedorDto facturaActualizada = facturaProveedorService.update(id, facturaProveedorConDetallesDto.getFactura());

        // Actualizar los detalles de la factura
        List<FacturaProveedorDetalleDto> detallesActualizados = facturaProveedorConDetallesDto.getDetalles().stream()
                .map(detalle -> facturaProveedorDetalleService.update(detalle.getId(), detalle))
                .collect(Collectors.toList());
        FacturaProveedorConDetallesDto nuevo = new FacturaProveedorConDetallesDto();
        nuevo.setFactura(facturaActualizada);
        nuevo.setDetalles(detallesActualizados);

        // Devolver la factura con los detalles actualizados
        return nuevo;
    }

    @Override
    //No hace falta implementar
    public boolean delete(Long id) {
        return false;
    }
}
