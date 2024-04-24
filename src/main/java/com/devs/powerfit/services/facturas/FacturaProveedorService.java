package com.devs.powerfit.services.facturas;

import com.devs.powerfit.beans.facturas.FacturaProveedorBean;
import com.devs.powerfit.daos.facturas.FacturaProveedorDao;
import com.devs.powerfit.dtos.facturas.FacturaDto;
import com.devs.powerfit.dtos.facturas.FacturaProveedorDto;
import com.devs.powerfit.dtos.proveedores.ProveedorDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.facturas.IFacturaProveedorService;
import com.devs.powerfit.services.proveedores.ProveedorService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.facturaMappers.FacturaProveedorMapper;
import com.devs.powerfit.utils.mappers.proveedorMapper.ProveedorMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Random;

@Service
@Transactional
public class FacturaProveedorService implements IFacturaProveedorService {
    private final FacturaProveedorDao facturaDao;
    private final FacturaProveedorMapper mapper;
    private final ProveedorService proveedorService;
    private final ProveedorMapper proveedorMapper;
    @Autowired
    public FacturaProveedorService(FacturaProveedorDao facturaDao, FacturaProveedorMapper mapper, ProveedorService proveedorService, ProveedorMapper proveedorMapper) {
        this.facturaDao = facturaDao;
        this.mapper = mapper;
        this.proveedorService = proveedorService;
        this.proveedorMapper = proveedorMapper;
    }

    @Override
    public FacturaProveedorDto create(FacturaProveedorDto facturaProveedorDto) {

        // Verificar si el proveedor existe
        ProveedorDto proveedorDto = proveedorService.getById(facturaProveedorDto.getProveedorId());
        if (proveedorDto == null) {
            throw new NotFoundException("El proveedor con ID " + facturaProveedorDto.getProveedorId() + " no existe");
        }

        // Calcular el ivaTotal si no se proporciona explícitamente
        double ivaTotal = facturaProveedorDto.getIvaTotal() != null ? facturaProveedorDto.getIvaTotal() : facturaProveedorDto.getIva5() + facturaProveedorDto.getIva10();

        // Verificar si los datos de ivaTotal y total son correctos
        if (facturaProveedorDto.getIvaTotal() != null && facturaProveedorDto.getIvaTotal() != ivaTotal) {
            throw new BadRequestException("El valor de ivaTotal proporcionado no coincide con el cálculo");
        }

        double total = facturaProveedorDto.getTotal() != null ? facturaProveedorDto.getTotal() : facturaProveedorDto.getSubTotal() + ivaTotal;

        if (facturaProveedorDto.getTotal() != null && facturaProveedorDto.getTotal() != total) {
            throw new BadRequestException("El valor de total proporcionado no coincide con el cálculo");
        }

        LocalDate fecha = facturaProveedorDto.getFecha() != null ? facturaProveedorDto.getFecha() :  LocalDate.now();
        // Crear una instancia de Factura desde FacturaDto
        FacturaProveedorBean factura = new FacturaProveedorBean();
        factura.setProveedor(proveedorMapper.toBean(proveedorDto));
        factura.setTimbrado(facturaProveedorDto.getTimbrado());
        factura.setNroFactura(this.generarNumeroFactura());
        factura.setNombreProveedor(facturaProveedorDto.getNombreProveedor());
        factura.setRucProveedor(facturaProveedorDto.getRucProveedor());
        factura.setFecha(fecha);
        factura.setTotal(total);
        factura.setSubTotal(facturaProveedorDto.getSubTotal() != null ? facturaProveedorDto.getSubTotal() : total - ivaTotal);
        factura.setSaldo(facturaProveedorDto.getSaldo() != null ? facturaProveedorDto.getSaldo() : 0.0);
        factura.setIva5(facturaProveedorDto.getIva5() != null ? facturaProveedorDto.getIva5() : 0.0);
        factura.setIva10(facturaProveedorDto.getIva10() != null ? facturaProveedorDto.getIva10() : 0.0);
        factura.setIvaTotal(ivaTotal);
        factura.setPagado(facturaProveedorDto.isPagado());
        factura.setActive(true);
        // Guardar la factura en la base de datos
        FacturaProveedorBean savedFactura = facturaDao.save(factura);

        // Retornar la FacturaDto creada
        return mapper.toDto(savedFactura);
    }

    @Override
    public FacturaProveedorDto getById(Long id) {
        var factura = facturaDao.findByIdAndActiveTrue(id);
        if (factura.isPresent()) {
            return mapper.toDto(factura.get());
        }
        throw new NotFoundException("Factura no encontrada");
    }

    @Override
    public PageResponse<FacturaProveedorDto> getAll(int page) {
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
    //No se implementa
    public FacturaProveedorDto update(Long id, FacturaProveedorDto facturaProveedorDto) {
        return null;
    }

    @Override
    //No se implementa
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public PageResponse<FacturaProveedorDto> searchByNombreProveedor(String nombre, int page) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var facturaPage = facturaDao.findAllByNombreProveedorContainingIgnoreCaseAndActiveIsTrue(pageRequest,nombre);
        if (facturaPage.isEmpty()) {
            throw new NotFoundException("No hay facturas en la lista");
        }
        var facturaDtoPage = facturaPage.map(mapper::toDto);
        return new PageResponse<>(facturaDtoPage.getContent(),
                facturaDtoPage.getTotalPages(),
                facturaDtoPage.getTotalElements(),
                facturaDtoPage.getNumber() + 1);
    }
    public FacturaProveedorDto actualizarSaldo(Long id, double nuevoSaldo) {
        // Verificar si la factura con el ID proporcionado existe
        FacturaProveedorBean factura = facturaDao.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("La factura con ID " + id + " no existe"));
        // Actualizar el saldo de la factura
        factura.setSaldo(nuevoSaldo);
        // Guardar los cambios en la base de datos
        FacturaProveedorBean facturaActualizada = facturaDao.save(factura);
        // Retornar la factura actualizada
        return mapper.toDto(facturaActualizada);
    }
    public FacturaProveedorDto modificarPagado(Long id, boolean pagado) {
        // Verificar si la factura con el ID proporcionado existe
        FacturaProveedorBean factura = facturaDao.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("La factura con ID " + id + " no existe"));
        // Actualizar el estado de pago de la factura
        factura.setPagado(pagado);
        // Guardar los cambios en la base de datos
        FacturaProveedorBean facturaActualizada = facturaDao.save(factura);
        // Retornar la factura actualizada
        return mapper.toDto(facturaActualizada);
    }

    @Override
    public PageResponse<FacturaProveedorDto> searchByRucProveedor(String ruc, int page) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var facturaPage = facturaDao.findAllByRucProveedorIgnoreCaseAndActiveTrue(pageRequest,ruc);
        if (facturaPage.isEmpty()) {
            throw new NotFoundException("No hay facturas en la lista");
        }
        var facturaDtoPage = facturaPage.map(mapper::toDto);
        return new PageResponse<>(facturaDtoPage.getContent(),
                facturaDtoPage.getTotalPages(),
                facturaDtoPage.getTotalElements(),
                facturaDtoPage.getNumber() + 1);
    }
    public PageResponse<FacturaProveedorDto> searchByPendiente( int page) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var facturaPage = facturaDao.findAllByPagado(pageRequest,false );
        if (facturaPage.isEmpty()) {
            throw new NotFoundException("No hay facturas en la lista");
        }
        var facturaDtoPage = facturaPage.map(mapper::toDto);
        return new PageResponse<>(facturaDtoPage.getContent(),
                facturaDtoPage.getTotalPages(),
                facturaDtoPage.getTotalElements(),
                facturaDtoPage.getNumber() + 1);
    }
    public PageResponse<FacturaProveedorDto> searchByPagado( int page) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var facturaPage = facturaDao.findAllByPagado(pageRequest,true );
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
    public FacturaProveedorDto searchByNumeroFactura(String numeroFactura) {
        var factura = facturaDao.findByNroFacturaIgnoreCaseAndActiveTrue(numeroFactura);
        if (factura.isPresent()) {
            return mapper.toDto(factura.get());
        }
        throw new NotFoundException("Factura no encontrada");
    }
    private String generarNumeroFactura() {
        Random random = new Random();

        // Generar el primer trío de dígitos aleatorios (limitado hasta 10)
        int primerTrio = random.nextInt(10) + 1; // Aseguramos que el número no sea cero
        String primerTrioStr = String.format("%03d", primerTrio);

        // Generar los dos siguientes tríos de dígitos aleatorios
        int segundoTrio = random.nextInt(999) + 1; // Aseguramos que el número no sea cero
        String segundoTrioStr = String.format("%03d", segundoTrio);

        // Generar el número octal aleatorio
        int octal = random.nextInt(99999999) + 1; // Aseguramos que el número no sea cero
        String octalStr = String.format("%08o", octal);

        // Construir el número de factura con el formato especificado
        return primerTrioStr + "-" + segundoTrioStr + "-" + octalStr;
    }
    public PageResponse<FacturaProveedorDto> searchByFecha(int page, LocalDate fechaInicio, LocalDate fechaFin) {
        // Validar que la fecha final sea igual o posterior a la fecha inicial
        if (fechaFin.isBefore(fechaInicio)) {
            throw new BadRequestException("La fecha final debe ser igual o posterior a la fecha inicial");
        }

        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var facturaPage = facturaDao.findAllByFechaBetween(pageRequest, fechaInicio, fechaFin);
        if (facturaPage.isEmpty()) {
            throw new NotFoundException("No hay facturas en la lista");
        }
        var facturaDtoPage = facturaPage.map(mapper::toDto);
        return new PageResponse<>(facturaDtoPage.getContent(),
                facturaDtoPage.getTotalPages(),
                facturaDtoPage.getTotalElements(),
                facturaDtoPage.getNumber() + 1);
    }
}
