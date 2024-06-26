package com.devs.powerfit.services.facturas;

import com.devs.powerfit.beans.facturas.FacturaProveedorBean;
import com.devs.powerfit.daos.facturas.FacturaProveedorDao;
import com.devs.powerfit.dtos.facturas.FacturaProveedorDto;
import com.devs.powerfit.dtos.facturas.filtros.ReporteComprasFilterDto;
import com.devs.powerfit.dtos.facturas.reportes.ReporteComprasDto;
import com.devs.powerfit.dtos.proveedores.ProveedorDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.facturas.IFacturaProveedorService;
import com.devs.powerfit.services.proveedores.ProveedorService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.facturaMappers.FacturaProveedorMapper;
import com.devs.powerfit.utils.mappers.proveedorMapper.ProveedorMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import com.devs.powerfit.utils.specifications.ReporteCompraSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
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

    @Override
    public PageResponse<FacturaProveedorDto> filtrarFacturas(Specification<FacturaProveedorBean> spec, int page) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var facturas = facturaDao.findAll(spec, pageRequest);
        var facturaDtoPage = facturas.map(mapper::toDto);
        return new PageResponse<>(facturaDtoPage.getContent(),
                facturaDtoPage.getTotalPages(),
                facturaDtoPage.getTotalElements(),
                facturaDtoPage.getNumber() + 1);
    }
    public ReporteComprasDto filtrarFacturas(ReporteComprasFilterDto filterDto, int page) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        Specification<FacturaProveedorBean> spec = Specification.where(ReporteCompraSpecification.isActive());

        if (filterDto.getNumeroFactura() != null && !filterDto.getNumeroFactura().isEmpty()) {
            spec = spec.and(ReporteCompraSpecification.hasNumeroFactura(filterDto.getNumeroFactura()));
        }
        if (filterDto.getFechaInicio() != null && filterDto.getFechaFin() != null) {
            spec = spec.and(ReporteCompraSpecification.hasFechaBetween(filterDto.getFechaInicio(), filterDto.getFechaFin()));
        }
        if (filterDto.getNombreProveedor() != null && !filterDto.getNombreProveedor().isEmpty()) {
            spec = spec.and(ReporteCompraSpecification.hasNombreProveedor(filterDto.getNombreProveedor()));
        }
        if (filterDto.getRucProveedor() != null && !filterDto.getRucProveedor().isEmpty()) {
            spec = spec.and(ReporteCompraSpecification.hasRucProveedor(filterDto.getRucProveedor()));
        }
        if (filterDto.getPagado() != null) {
            spec = spec.and(ReporteCompraSpecification.isPagado(filterDto.getPagado()));
        }

        Page<FacturaProveedorBean> facturasPage = facturaDao.findAll(spec, pageRequest);

        // Calcular los totalizadores
        List<FacturaProveedorBean> allFacturas = facturaDao.findAll(spec);
        double total = allFacturas.stream().mapToDouble(FacturaProveedorBean::getTotal).sum();
        double totalPagado = allFacturas.stream().filter(FacturaProveedorBean::isPagado).mapToDouble(FacturaProveedorBean::getTotal).sum();
        double totalPendiente = allFacturas.stream().filter(factura -> !factura.isPagado()).mapToDouble(FacturaProveedorBean::getTotal).sum();

        List<FacturaProveedorDto> facturaDtoList = facturasPage.map(mapper::toDto).getContent();

        return new ReporteComprasDto(
                total,
                totalPagado,
                totalPendiente,
                facturaDtoList,
                facturasPage.getTotalPages(),
                facturasPage.getTotalElements(),
                facturasPage.getNumber() + 1
        );
    }



    private String generarNumeroFactura() {
        Random random = new Random();

        // Generar el primer trío de dígitos aleatorios (entre 0 y 999)
        int primerTrio = random.nextInt(1000); // 0 a 999
        String primerTrioStr = String.format("%03d", primerTrio);

        // Generar el segundo trío de dígitos aleatorios (entre 0 y 999)
        int segundoTrio = random.nextInt(1000); // 0 a 999
        String segundoTrioStr = String.format("%03d", segundoTrio);

        // Generar el número decimal aleatorio de 8 dígitos
        int octal = random.nextInt(100000000); // 0 a 99999999
        String octalStr = String.format("%08d", octal);

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
