package com.devs.powerfit.services.facturas;

import com.devs.powerfit.beans.facturas.FacturaBean;
import com.devs.powerfit.beans.facturas.FacturaDetalleBean;
import com.devs.powerfit.daos.facturas.FacturaDao;
import com.devs.powerfit.daos.facturas.FacturaDetalleDao;
import com.devs.powerfit.dtos.facturas.FacturaDetalleDto;
import com.devs.powerfit.dtos.productos.ProductoDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.facturas.IFacturaDetalleService;
import com.devs.powerfit.services.productos.ProductoService;
import com.devs.powerfit.services.suscripciones.SuscripcionService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.facturaMappers.FacturaDetalleMapper;
import com.devs.powerfit.utils.mappers.productoMapper.ProductoMapper;
import com.devs.powerfit.utils.mappers.suscipcioneMapper.SuscripcionMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FacturaDetalleService implements IFacturaDetalleService {
    private final FacturaDao facturaDao;
    private final FacturaDetalleDao facturaDetalleDao;
    private final SuscripcionService suscripcionService;
    private final ProductoService productoService;
    private final FacturaDetalleMapper mapper;
    private final ProductoMapper productoMapper;
    private final SuscripcionMapper suscripcionMapper;

    public FacturaDetalleService(FacturaDao facturaDao, FacturaDetalleDao facturaDetalleDao, SuscripcionService suscripcionService, ProductoService productoService, FacturaDetalleMapper mapper, ProductoMapper productoMapper, SuscripcionMapper suscripcionMapper) {
        this.facturaDao = facturaDao;
        this.facturaDetalleDao = facturaDetalleDao;
        this.suscripcionService = suscripcionService;
        this.productoService = productoService;
        this.mapper = mapper;
        this.productoMapper = productoMapper;
        this.suscripcionMapper = suscripcionMapper;
    }

    @Override
    public FacturaDetalleDto create(FacturaDetalleDto facturaDetalleDto) {
        // Verificar si los campos obligatorios no están incompletos
        if (facturaDetalleDto.getFacturaId() == null || (facturaDetalleDto.getProductoId() == null && facturaDetalleDto.getSuscripcionId() == null) || facturaDetalleDto.getPrecioUnitario() == null || facturaDetalleDto.getCantidad() == null) {
            throw new BadRequestException("Los campos facturaId, productoId o suscripcionId, precioUnitario y cantidad son obligatorios para crear un nuevo detalle de factura");
        }

        // Verificar si la factura existe
        var facturaBean = facturaDao.findByIdAndActiveTrue(facturaDetalleDto.getFacturaId());
        if (facturaBean.isEmpty()) {
            throw new NotFoundException("La factura con ID " + facturaDetalleDto.getFacturaId() + " no existe");
        }

        // Verificar si se trata de un producto
        if (facturaDetalleDto.getProductoId() != null) {
            var productoDto = productoService.getById(facturaDetalleDto.getProductoId());
            if (productoDto == null) {
                throw new NotFoundException("El producto con ID " + facturaDetalleDto.getProductoId() + " no existe");
            }

            // Verificar si la cantidad disponible en el stock es suficiente
            if (!(productoDto.getCantidad() >= facturaDetalleDto.getCantidad())) {
                throw new BadRequestException("La cantidad que desea vender supera la disponible en el stock del producto");
            }

            // Actualizar el stock del producto
            productoDto.setCantidad(productoDto.getCantidad() - facturaDetalleDto.getCantidad());
            ProductoDto newProductoDto = productoService.update(productoDto.getId(), productoDto);

            // Calcular el subtotal y el total de IVA
            double subTotal = facturaDetalleDto.getPrecioUnitario() * facturaDetalleDto.getCantidad();
            double ivaTotal = subTotal * facturaDetalleDto.getIva();

            // Verificar si los datos coinciden con los proporcionados por el front
            if (facturaDetalleDto.getSubtotal() != subTotal || facturaDetalleDto.getIvaTotal() != ivaTotal) {
                throw new BadRequestException("Los datos no coinciden con los enviados por el frontend");
            }

            // Crear el detalle de factura para el producto
            FacturaDetalleBean facturaDetalleBean = new FacturaDetalleBean();
            facturaDetalleBean.setActive(true);
            facturaDetalleBean.setFactura(facturaBean.get());
            facturaDetalleBean.setProducto(productoMapper.toBean(newProductoDto));
            facturaDetalleBean.setPrecioUnitario(facturaDetalleDto.getPrecioUnitario());
            facturaDetalleBean.setCantidad(facturaDetalleDto.getCantidad());
            facturaDetalleBean.setSubTotal(subTotal);
            facturaDetalleBean.setIva(facturaDetalleDto.getIva());
            facturaDetalleBean.setIvaTotal(ivaTotal);
            FacturaDetalleBean savedFacturaDetalle = facturaDetalleDao.save(facturaDetalleBean);

            // Retornar el FacturaDetalleDto creado
            return mapper.toDto(savedFacturaDetalle);
        } else if (facturaDetalleDto.getSuscripcionId() != null) {
            if(facturaBean.get().getCliente()==null){
                throw new BadRequestException("No se puede pagar la suscripcion en una factura sin cliente");
            }
            // Verificar si ya existe un detalle de factura para esta suscripción en la misma factura principal
            boolean suscripcionExistente = facturaDetalleDao.existsByFacturaIdAndSuscripcionId(facturaDetalleDto.getFacturaId(), facturaDetalleDto.getSuscripcionId());
            if (suscripcionExistente) {
                throw new BadRequestException("La suscripción ya ha sido agregada previamente a esta factura");
            }

            // Verificar si se trata de una suscripción
            var suscripcionDto = suscripcionService.getById(facturaDetalleDto.getSuscripcionId());
            if (suscripcionDto == null) {
                throw new NotFoundException("La suscripción con ID " + facturaDetalleDto.getSuscripcionId() + " no existe");
            }

            // Calcular el subtotal y el total de IVA (en el caso de suscripción, siempre será 0.10)
            double subTotal = facturaDetalleDto.getPrecioUnitario() * facturaDetalleDto.getCantidad();
            double ivaTotal = subTotal * 0.10;

            // Verificar si los datos coinciden con los proporcionados por el front
            if (facturaDetalleDto.getSubtotal() != subTotal || facturaDetalleDto.getIvaTotal() != ivaTotal) {
                throw new BadRequestException("Los datos no coinciden con los enviados por el frontend");
            }

            // Crear el detalle de factura para la suscripción
            FacturaDetalleBean facturaDetalleBean = new FacturaDetalleBean();
            facturaDetalleBean.setActive(true);
            facturaDetalleBean.setFactura(facturaBean.get());
            facturaDetalleBean.setSuscripcion(suscripcionMapper.toBean(suscripcionDto));
            facturaDetalleBean.setPrecioUnitario(facturaDetalleDto.getPrecioUnitario());
            facturaDetalleBean.setCantidad(facturaDetalleDto.getCantidad());
            facturaDetalleBean.setSubTotal(subTotal);
            facturaDetalleBean.setIva(0.10); // IVA fijo para suscripción
            facturaDetalleBean.setIvaTotal(ivaTotal);
            FacturaDetalleBean savedFacturaDetalle = facturaDetalleDao.save(facturaDetalleBean);

            // Retornar el FacturaDetalleDto creado
            return mapper.toDto(savedFacturaDetalle);
        } else {
            throw new BadRequestException("Debe especificar un producto o una suscripción para el detalle de la factura");
        }
    }


    @Override
    public FacturaDetalleDto getById(Long id) {
        var factura = facturaDetalleDao.findByIdAndActiveTrue(id);
        if (factura.isPresent()) {
            return mapper.toDto(factura.get());
        }
        throw new NotFoundException("Factura Detalle no encontrada");
    }

    @Override
    public PageResponse<FacturaDetalleDto> getAll(int page) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var facturaPage = facturaDetalleDao.findAllByActiveTrue(pageRequest);
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
    public FacturaDetalleDto update(Long id, FacturaDetalleDto facturaDetalleDto) {
        // Verificar si el ID del detalle de factura existe
        FacturaDetalleBean facturaDetalleBean = facturaDetalleDao.findById(id)
                .orElseThrow(() -> new NotFoundException("El detalle de factura con ID " + id + " no existe"));

        // Verificar si se trata de un producto
        if (facturaDetalleDto.getProductoId() != null) {
            var productoDto = productoService.getById(facturaDetalleDto.getProductoId());
            if (productoDto == null) {
                throw new NotFoundException("El producto con ID " + facturaDetalleDto.getProductoId() + " no existe");
            }

            // Actualizar el producto asociado al detalle de factura
            facturaDetalleBean.setProducto(productoMapper.toBean(productoDto));
        } else if (facturaDetalleDto.getSuscripcionId() != null) {
            // Verificar si se trata de una suscripción
            var suscripcionDto = suscripcionService.getById(facturaDetalleDto.getSuscripcionId());
            if (suscripcionDto == null) {
                throw new NotFoundException("La suscripción con ID " + facturaDetalleDto.getSuscripcionId() + " no existe");
            }

            // Actualizar la suscripción asociada al detalle de factura
            facturaDetalleBean.setSuscripcion(suscripcionMapper.toBean(suscripcionDto));
        } else {
            throw new BadRequestException("Debe especificar un producto o una suscripción para actualizar el detalle de la factura");
        }

        // Actualizar los demás campos del detalle de factura
        facturaDetalleBean.setPrecioUnitario(facturaDetalleDto.getPrecioUnitario());
        facturaDetalleBean.setCantidad(facturaDetalleDto.getCantidad());
        facturaDetalleBean.setSubTotal(facturaDetalleDto.getSubtotal());
        facturaDetalleBean.setIva(facturaDetalleDto.getIva());
        facturaDetalleBean.setIvaTotal(facturaDetalleDto.getIvaTotal());

        // Guardar el detalle de factura actualizado en la base de datos
        FacturaDetalleBean savedFacturaDetalle = facturaDetalleDao.save(facturaDetalleBean);

        // Retornar el detalle de factura actualizado
        return mapper.toDto(savedFacturaDetalle);
    }


    @Override
    public boolean delete(Long id) {
        return false;
    }
    public List<FacturaDetalleDto> getAllByFacturaId(Long facturaId) {
        var facturaDetalleBeanList = facturaDetalleDao.findAllByFacturaIdAndActiveTrue(facturaId);
        if (facturaDetalleBeanList.isEmpty()) {
            throw new NotFoundException("No hay facturas en la lista");
        }
        return facturaDetalleBeanList.stream().map(mapper::toDto).toList();
    }


    @Override
    public List<FacturaDetalleDto> getAllDetalles() {
        List<FacturaDetalleBean> detalles = facturaDetalleDao.findAllByProductoIsNotNull();
        return detalles.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<FacturaDetalleDto> getAllDetallesBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        // Obtener todas las facturas dentro del rango de fechas
        List<FacturaBean> facturasEnRango = facturaDao.findAllByFechaBetweenAndActiveTrue(fechaInicio, fechaFin);

        // Obtener y concatenar todos los detalles de las facturas

        return facturasEnRango.stream()
                .map(factura -> facturaDetalleDao.findAllByFacturaIdAndProductoIsNotNullAndActiveTrue(factura.getId()))
                .flatMap(List::stream)
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

}
