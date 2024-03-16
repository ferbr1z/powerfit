package com.devs.powerfit.services.facturas;

import com.devs.powerfit.beans.facturas.FacturaDetalleBean;
import com.devs.powerfit.beans.facturas.FacturaProveedorDetalleBean;
import com.devs.powerfit.daos.facturas.FacturaProveedorDao;
import com.devs.powerfit.daos.facturas.FacturaProveedorDetalleDao;
import com.devs.powerfit.dtos.facturas.FacturaProveedorDetalleDto;
import com.devs.powerfit.dtos.productos.ProductoDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.facturas.IFacturaProveedorDetalleService;
import com.devs.powerfit.services.productos.ProductoService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.facturaMappers.FacturaProveedorDetalleMapper;
import com.devs.powerfit.utils.mappers.productoMapper.ProductoMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class FacturaProveedorDetalleService implements IFacturaProveedorDetalleService {
    private final FacturaProveedorDao facturaDao;
    private final FacturaProveedorDetalleDao facturaDetalleDao;
    private final ProductoService productoService;
    private final FacturaProveedorDetalleMapper mapper;
    private final ProductoMapper productoMapper;

    public FacturaProveedorDetalleService(FacturaProveedorDao facturaDao, FacturaProveedorDetalleDao facturaDetalleDao, ProductoService productoService, FacturaProveedorDetalleMapper mapper, ProductoMapper productoMapper) {
        this.facturaDao = facturaDao;
        this.facturaDetalleDao = facturaDetalleDao;
        this.productoService = productoService;
        this.mapper = mapper;
        this.productoMapper = productoMapper;
    }

    @Override
    public FacturaProveedorDetalleDto create(FacturaProveedorDetalleDto facturaProveedorDetalleDto) {
        // Verificar si la factura existe
        var facturaBean = facturaDao.findByIdAndActiveTrue(facturaProveedorDetalleDto.getFacturaId());
        if (facturaBean.isEmpty()) {
            throw new NotFoundException("La factura con ID " + facturaProveedorDetalleDto.getFacturaId() + " no existe");
        }
        //Verificar si el producto existe
        var productoDto = productoService.getById(facturaProveedorDetalleDto.getProductoId());
        if (productoDto == null) {
            throw new NotFoundException("El producto con ID " + facturaProveedorDetalleDto.getProductoId() + " no existe");
        }

            // Actualizar el stock del producto
        productoDto.setCantidad(productoDto.getCantidad() + facturaProveedorDetalleDto.getCantidad());
        ProductoDto newProductoDto = productoService.update(productoDto.getId(), productoDto);
        // Calcular el subtotal y el total de IVA
        double subTotal = facturaProveedorDetalleDto.getPrecioUnitario() * facturaProveedorDetalleDto.getCantidad();
        double ivaTotal = subTotal * facturaProveedorDetalleDto.getIva();
        // Verificar si los datos coinciden con los proporcionados por el front
        if (facturaProveedorDetalleDto.getSubtotal() != subTotal || facturaProveedorDetalleDto.getIvaTotal() != ivaTotal) {
            throw new BadRequestException("Los datos no coinciden con los enviados por el frontend");
        }
        // Crear el detalle de factura para el producto
        FacturaProveedorDetalleBean facturaDetalleBean = new FacturaProveedorDetalleBean();
        facturaDetalleBean.setActive(true);
        facturaDetalleBean.setFactura(facturaBean.get());
        facturaDetalleBean.setProducto(productoMapper.toBean(newProductoDto));
        facturaDetalleBean.setPrecioUnitario(facturaProveedorDetalleDto.getPrecioUnitario());
        facturaDetalleBean.setCantidad(facturaProveedorDetalleDto.getCantidad());
        facturaDetalleBean.setSubTotal(subTotal);
        facturaDetalleBean.setIva(facturaProveedorDetalleDto.getIva());
        facturaDetalleBean.setIvaTotal(ivaTotal);
        FacturaProveedorDetalleBean savedFacturaDetalle = facturaDetalleDao.save(facturaDetalleBean);
        // Retornar el FacturaDetalleDto creado
        return mapper.toDto(savedFacturaDetalle);
    }

    @Override
    public FacturaProveedorDetalleDto getById(Long id) {
        var factura = facturaDetalleDao.findByIdAndActiveTrue(id);
        if (factura.isPresent()) {
            return mapper.toDto(factura.get());
        }
        throw new NotFoundException("Factura Detalle no encontrada");
    }

    @Override
    public PageResponse<FacturaProveedorDetalleDto> getAll(int page) {
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
    //No se implementa ya que no esta permitido modificar una factura
    public FacturaProveedorDetalleDto update(Long id, FacturaProveedorDetalleDto facturaProveedorDetalleDto) {
        return null;
    }

    @Override
    //No se implementa ya que no esta permitido eliminar una factura
    public boolean delete(Long id) {
        return false;
    }
    public List<FacturaProveedorDetalleDto> getAllByFacturaId(Long facturaId) {
        var facturaDetalleBeanList = facturaDetalleDao.findAllByFacturaIdAndActiveTrue(facturaId);
        if (facturaDetalleBeanList.isEmpty()) {
            throw new NotFoundException("No hay facturas en la lista");
        }
        return facturaDetalleBeanList.stream().map(mapper::toDto).toList();
    }
}
