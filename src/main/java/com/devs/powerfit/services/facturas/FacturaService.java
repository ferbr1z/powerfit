package com.devs.powerfit.services.facturas;

import com.devs.powerfit.beans.facturas.FacturaBean;
import com.devs.powerfit.daos.facturas.FacturaDao;
import com.devs.powerfit.dtos.actividades.ActividadDto;
import com.devs.powerfit.dtos.clientes.ClienteDto;
import com.devs.powerfit.dtos.facturas.FacturaDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.facturas.IFacturaService;
import com.devs.powerfit.services.clientes.ClienteService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.clienteMappers.ClienteMapper;
import com.devs.powerfit.utils.mappers.facturaMappers.FacturaMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FacturaService implements IFacturaService {
    private final FacturaDao facturaDao;
    private final FacturaMapper mapper;
    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;
    @Autowired
    public FacturaService(FacturaDao facturaDao, FacturaMapper mapper, ClienteService clienteService, ClienteMapper clienteMapper) {
        this.facturaDao = facturaDao;
        this.mapper = mapper;
        this.clienteService = clienteService;
        this.clienteMapper = clienteMapper;
    }
    @Override
    public FacturaDto create(FacturaDto facturaDto) {
        // Verificar si los campos obligatorios no están incompletos
        if (facturaDto.getClienteId() == null || facturaDto.getTimbrado() == null || facturaDto.getNroFactura() == null || facturaDto.getTotal() == null) {
            throw new BadRequestException("Los campos clienteId, timbrado, nroFactura y total son obligatorios para crear una nueva factura");
        }

        // Verificar si el cliente existe
        ClienteDto clienteDto = clienteService.getById(facturaDto.getClienteId());
        if (clienteDto == null) {
            throw new NotFoundException("El cliente con ID " + facturaDto.getClienteId() + " no existe");
        }

        // Verificar si la factura ya existe
        if (facturaDao.existsByNroFactura(facturaDto.getNroFactura())) {
            throw new BadRequestException("Ya existe una factura con el número " + facturaDto.getNroFactura());
        }

        // Comprobar que los valores no sean negativos
        if (facturaDto.getSubTotal() < 0 || facturaDto.getIva5() < 0 || facturaDto.getIva10() < 0 || facturaDto.getTotal() < 0) {
            throw new BadRequestException("Los valores subTotal, iva5, iva10 y total no pueden ser negativos");
        }

        // Calcular el ivaTotal si no se proporciona explícitamente
        double ivaTotal = facturaDto.getIvaTotal() != null ? facturaDto.getIvaTotal() : facturaDto.getIva5() + facturaDto.getIva10();

        // Verificar si los datos de ivaTotal y total son correctos
        if (facturaDto.getIvaTotal() != null && facturaDto.getIvaTotal() != ivaTotal) {
            throw new BadRequestException("El valor de ivaTotal proporcionado no coincide con el cálculo");
        }

        double total = facturaDto.getTotal() != null ? facturaDto.getTotal() : facturaDto.getSubTotal() + ivaTotal;

        if (facturaDto.getTotal() != null && facturaDto.getTotal() != total) {
            throw new BadRequestException("El valor de total proporcionado no coincide con el cálculo");
        }

        // Convertir la fecha de String a Date
        // Convertir la fecha de String a Date
        Date fecha;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            fecha = facturaDto.getFecha() != null ? dateFormat.parse(dateFormat.format(facturaDto.getFecha())) : new Date();
        } catch (ParseException e) {
            throw new BadRequestException("Error al convertir la fecha");
        }



        // Crear una instancia de Factura desde FacturaDto
        FacturaBean factura = new FacturaBean();
        factura.setCliente(clienteMapper.toBean(clienteDto));
        factura.setTimbrado(facturaDto.getTimbrado());
        factura.setNroFactura(facturaDto.getNroFactura());
        factura.setNombreCliente(facturaDto.getNombreCliente());
        factura.setRucCliente(facturaDto.getRucCliente());
        factura.setFecha(fecha);
        factura.setTotal(total);
        factura.setSubTotal(facturaDto.getSubTotal() != null ? facturaDto.getSubTotal() : total - ivaTotal);
        factura.setSaldo(facturaDto.getSaldo() != null ? facturaDto.getSaldo() : 0.0);
        factura.setIva5(facturaDto.getIva5() != null ? facturaDto.getIva5() : 0.0);
        factura.setIva10(facturaDto.getIva10() != null ? facturaDto.getIva10() : 0.0);
        factura.setIvaTotal(ivaTotal);
        factura.setPagado(facturaDto.isPagado());
        factura.setActive(true);

        // Guardar la factura en la base de datos
        FacturaBean savedFactura = facturaDao.save(factura);

        // Retornar la FacturaDto creada
        return mapper.toDto(savedFactura);
    }


    @Override
    public FacturaDto getById(Long id) {
        var factura = facturaDao.findByIdAndActiveTrue(id);
        if (factura.isPresent()) {
            return mapper.toDto(factura.get());
        }
        throw new NotFoundException("Factura no encontrada");
    }


    @Override
    public PageResponse<FacturaDto> getAll(int page) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var facturaPage = facturaDao.findAllByActiveTrue(pageRequest);
        if (facturaPage.isEmpty()) {
            throw new NotFoundException("No hay facturas en la lista");
        }
        var facturaDtoPage = facturaPage.map(mapper::toDto);
        return new PageResponse<>(facturaDtoPage.getContent(),
                facturaDtoPage.getTotalPages(),
                facturaDtoPage.getTotalElements(),
                facturaDtoPage.getNumber() + 1);
    }


    @Override
    public FacturaDto update(Long id, FacturaDto facturaDto) {
        // Verificar si la factura con el ID proporcionado existe
        FacturaBean existingFactura = facturaDao.findById(id)
                .orElseThrow(() -> new NotFoundException("La factura con ID " + id + " no existe"));

        // Verificar si los campos obligatorios no están incompletos
        if (facturaDto.getClienteId() == null || facturaDto.getTimbrado() == null || facturaDto.getNroFactura() == null || facturaDto.getTotal() == null) {
            throw new BadRequestException("Los campos clienteId, timbrado, nroFactura y total son obligatorios para actualizar una factura");
        }

        // Verificar si el cliente existe
        ClienteDto clienteDto = clienteService.getById(facturaDto.getClienteId());
        if (clienteDto == null) {
            throw new NotFoundException("El cliente con ID " + facturaDto.getClienteId() + " no existe");
        }

        // Verificar si la factura ya existe con el nuevo número de factura
        if (!existingFactura.getNroFactura().equals(facturaDto.getNroFactura()) && facturaDao.existsByNroFactura(facturaDto.getNroFactura())) {
            throw new BadRequestException("Ya existe una factura con el número " + facturaDto.getNroFactura());
        }

        // Comprobar que los valores no sean negativos
        if (facturaDto.getSubTotal() < 0 || facturaDto.getIva5() < 0 || facturaDto.getIva10() < 0 || facturaDto.getTotal() < 0) {
            throw new BadRequestException("Los valores subTotal, iva5, iva10 y total no pueden ser negativos");
        }

        // Calcular el ivaTotal si no se proporciona explícitamente
        double ivaTotal = facturaDto.getIvaTotal() != null ? facturaDto.getIvaTotal() : facturaDto.getIva5() + facturaDto.getIva10();

        // Verificar si los datos de ivaTotal y total son correctos
        if (facturaDto.getIvaTotal() != null && facturaDto.getIvaTotal() != ivaTotal) {
            throw new BadRequestException("El valor de ivaTotal proporcionado no coincide con el cálculo");
        }

        double total = facturaDto.getTotal() != null ? facturaDto.getTotal() : facturaDto.getSubTotal() + ivaTotal;

        if (facturaDto.getTotal() != null && facturaDto.getTotal() != total) {
            throw new BadRequestException("El valor de total proporcionado no coincide con el cálculo");
        }

        // Convertir la fecha de String a Date
        Date fecha;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            fecha = facturaDto.getFecha() != null ? dateFormat.parse(dateFormat.format(facturaDto.getFecha())) : new Date();
        } catch (ParseException e) {
            throw new BadRequestException("Error al convertir la fecha");
        }


        // Actualizar la factura con los nuevos valores
        existingFactura.setCliente(clienteMapper.toBean(clienteDto));
        existingFactura.setTimbrado(facturaDto.getTimbrado());
        existingFactura.setNroFactura(facturaDto.getNroFactura());
        existingFactura.setNombreCliente(facturaDto.getNombreCliente());
        existingFactura.setRucCliente(facturaDto.getRucCliente());
        existingFactura.setFecha(fecha);
        existingFactura.setTotal(total);
        existingFactura.setSubTotal(facturaDto.getSubTotal() != null ? facturaDto.getSubTotal() : total - ivaTotal);
        existingFactura.setSaldo(facturaDto.getSaldo() != null ? facturaDto.getSaldo() : 0.0);
        existingFactura.setIva5(facturaDto.getIva5() != null ? facturaDto.getIva5() : 0.0);
        existingFactura.setIva10(facturaDto.getIva10() != null ? facturaDto.getIva10() : 0.0);
        existingFactura.setIvaTotal(ivaTotal);
        existingFactura.setPagado(facturaDto.isPagado());

        // Guardar los cambios en la base de datos
        FacturaBean updatedFactura = facturaDao.save(existingFactura);

        // Retornar la FacturaDto actualizada
        return mapper.toDto(updatedFactura);
    }

    @Override
    public boolean delete(Long id) {
        var factura = facturaDao.findByIdAndActiveTrue(id);
        if (factura.isPresent()) {
            var facturaBean = factura.get();
            facturaBean.setActive(false);
            facturaDao.save(facturaBean);
            return true;
        }
        throw new NotFoundException("factura no encontrada");
    }

    @Override
    public PageResponse<FacturaDto> searchByNombreCliente(String nombre, int page) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var facturaPage = facturaDao.findAllByNombreClienteContainingIgnoreCaseAndActiveIsTrue(pageRequest,nombre);
        if (facturaPage.isEmpty()) {
            throw new NotFoundException("No hay facturas en la lista");
        }
        var facturaDtoPage = facturaPage.map(mapper::toDto);
        return new PageResponse<>(facturaDtoPage.getContent(),
                facturaDtoPage.getTotalPages(),
                facturaDtoPage.getTotalElements(),
                facturaDtoPage.getNumber() + 1);
    }

    @Override
    public PageResponse<FacturaDto> searchByRucCliente(String ruc, int page) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var facturaPage = facturaDao.findAllByRucClienteIgnoreCaseAndActiveTrue(pageRequest,ruc);
        if (facturaPage.isEmpty()) {
            throw new NotFoundException("No hay facturas en la lista");
        }
        var facturaDtoPage = facturaPage.map(mapper::toDto);
        return new PageResponse<>(facturaDtoPage.getContent(),
                facturaDtoPage.getTotalPages(),
                facturaDtoPage.getTotalElements(),
                facturaDtoPage.getNumber() + 1);
    }

    @Override
    public FacturaDto searchByNumeroFactura(String numeroFactura) {
        var factura = facturaDao.findByNroFacturaIgnoreCaseAndActiveTrue(numeroFactura);
        if (factura.isPresent()) {
            return mapper.toDto(factura.get());
        }
        throw new NotFoundException("Factura no encontrada");
    }

}
