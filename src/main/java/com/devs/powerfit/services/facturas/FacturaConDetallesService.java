package com.devs.powerfit.services.facturas;

import com.devs.powerfit.dtos.facturas.FacturaConDetallesDto;
import com.devs.powerfit.dtos.facturas.FacturaDetalleDto;
import com.devs.powerfit.dtos.facturas.FacturaDto;
import com.devs.powerfit.interfaces.facturas.IFacturaConDetallesService;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FacturaConDetallesService implements IFacturaConDetallesService {

    private final FacturaService facturaService;
    private final FacturaDetalleService facturaDetalleService;

    public FacturaConDetallesService(FacturaService facturaService, FacturaDetalleService facturaDetalleService) {
        this.facturaService = facturaService;
        this.facturaDetalleService = facturaDetalleService;
    }

    @Override
    public FacturaConDetallesDto create(FacturaConDetallesDto facturaConDetallesDto) {
        // Crear la factura principal
        FacturaDto facturaCreada = facturaService.create(facturaConDetallesDto.getFactura());

        // Crear los detalles de la factura
        List<FacturaDetalleDto> detallesCreados = facturaConDetallesDto.getDetalles().stream()
                .map(detalle -> {
                    detalle.setFacturaId(facturaCreada.getId()); // Asignar el ID de la factura principal al detalle
                    return facturaDetalleService.create(detalle);
                })
                .collect(Collectors.toList());
        FacturaConDetallesDto nuevo = new FacturaConDetallesDto();
        nuevo.setFactura(facturaCreada);
        nuevo.setDetalles(detallesCreados);

        // Devolver la factura con los detalles creados
        return nuevo;
    }

    @Override
    public FacturaConDetallesDto getById(Long id) {
        // Obtener la factura principal por su ID
        FacturaDto facturaDto = facturaService.getById(id);
        // Obtener los detalles de la factura por el ID de la factura principal
        List<FacturaDetalleDto> detallesDto = facturaDetalleService.getAllByFacturaId(id);
        FacturaConDetallesDto nuevo = new FacturaConDetallesDto();
        nuevo.setFactura(facturaDto);
        nuevo.setDetalles(detallesDto);
        // Devolver la factura con los detalles
        return nuevo;
    }

    public FacturaConDetallesDto getByNumeroFactura(String numero) {
        // Obtener la factura principal por su ID
        FacturaDto facturaDto = facturaService.searchByNumeroFactura(numero);
        // Obtener los detalles de la factura por el ID de la factura principal
        List<FacturaDetalleDto> detallesDto = facturaDetalleService.getAllByFacturaId(facturaDto.getId());
        FacturaConDetallesDto nuevo = new FacturaConDetallesDto();
        nuevo.setFactura(facturaDto);
        nuevo.setDetalles(detallesDto);
        // Devolver la factura con los detalles
        return nuevo;
    }

    @Override
    public PageResponse<FacturaConDetallesDto> getAll(int page) {
        // Obtener todas las facturas principales
        PageResponse<FacturaDto> facturaPage = facturaService.getAll(page);

        // Para cada factura, obtener sus detalles
        List<FacturaConDetallesDto> facturaConDetallesList = facturaPage.getItems().stream()
                .map(facturaDto -> getById(facturaDto.getId())) // Obtener la factura con detalles
                .collect(Collectors.toList());

        // Devolver la lista de facturas con detalles
        return new PageResponse<>(facturaConDetallesList, facturaPage.getTotalPages(), facturaPage.getTotalItems(), facturaPage.getCurrentPage());
    }


    @Override
    public FacturaConDetallesDto update(Long id, FacturaConDetallesDto facturaConDetallesDto) {
        // Actualizar la factura principal
        FacturaDto facturaActualizada = facturaService.update(id, facturaConDetallesDto.getFactura());

        // Actualizar los detalles de la factura
        List<FacturaDetalleDto> detallesActualizados = facturaConDetallesDto.getDetalles().stream()
                .map(detalle -> facturaDetalleService.update(detalle.getId(), detalle))
                .collect(Collectors.toList());
        FacturaConDetallesDto nuevo = new FacturaConDetallesDto();
        nuevo.setFactura(facturaActualizada);
        nuevo.setDetalles(detallesActualizados);

        // Devolver la factura con los detalles actualizados
        return nuevo;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
    public PageResponse<FacturaConDetallesDto> getAllByNombreCliente(String nombre,int page) {
        // Obtener todas las facturas principales
        PageResponse<FacturaDto> facturaPage = facturaService.searchByNombreCliente(nombre,page);

        // Para cada factura, obtener sus detalles
        List<FacturaConDetallesDto> facturaConDetallesList = facturaPage.getItems().stream()
                .map(facturaDto -> getById(facturaDto.getId())) // Obtener la factura con detalles
                .collect(Collectors.toList());

        // Devolver la lista de facturas con detalles
        return new PageResponse<>(facturaConDetallesList, facturaPage.getTotalPages(), facturaPage.getTotalItems(), facturaPage.getCurrentPage());
    }

    @Override
    public PageResponse<FacturaConDetallesDto> getAllByRucCliente(String ruc, int page) {
        // Obtener todas las facturas principales
        PageResponse<FacturaDto> facturaPage = facturaService.searchByRucCliente(ruc,page);

        // Para cada factura, obtener sus detalles
        List<FacturaConDetallesDto> facturaConDetallesList = facturaPage.getItems().stream()
                .map(facturaDto -> getById(facturaDto.getId())) // Obtener la factura con detalles
                .collect(Collectors.toList());

        // Devolver la lista de facturas con detalles
        return new PageResponse<>(facturaConDetallesList, facturaPage.getTotalPages(), facturaPage.getTotalItems(), facturaPage.getCurrentPage());
    }
    public PageResponse<FacturaConDetallesDto> searchByPagado(int page) {
        // Obtener todas las facturas principales
        PageResponse<FacturaDto> facturaPage = facturaService.searchByPagado(page);

        // Para cada factura, obtener sus detalles
        List<FacturaConDetallesDto> facturaConDetallesList = facturaPage.getItems().stream()
                .map(facturaDto -> getById(facturaDto.getId())) // Obtener la factura con detalles
                .collect(Collectors.toList());

        // Devolver la lista de facturas con detalles
        return new PageResponse<>(facturaConDetallesList, facturaPage.getTotalPages(), facturaPage.getTotalItems(), facturaPage.getCurrentPage());
    }
    public PageResponse<FacturaConDetallesDto> searchByPendiente(int page) {
        // Obtener todas las facturas principales
        PageResponse<FacturaDto> facturaPage = facturaService.searchByPendiente(page);

        // Para cada factura, obtener sus detalles
        List<FacturaConDetallesDto> facturaConDetallesList = facturaPage.getItems().stream()
                .map(facturaDto -> getById(facturaDto.getId())) // Obtener la factura con detalles
                .collect(Collectors.toList());

        // Devolver la lista de facturas con detalles
        return new PageResponse<>(facturaConDetallesList, facturaPage.getTotalPages(), facturaPage.getTotalItems(), facturaPage.getCurrentPage());
    }
}
