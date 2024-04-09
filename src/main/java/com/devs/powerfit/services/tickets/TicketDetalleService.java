package com.devs.powerfit.services.tickets;

import com.devs.powerfit.beans.tickets.TicketBean;
import com.devs.powerfit.beans.tickets.TicketDetalleBean;
import com.devs.powerfit.daos.tickets.TicketDao;
import com.devs.powerfit.daos.tickets.TicketDetalleDao;
import com.devs.powerfit.dtos.productos.ProductoDto;
import com.devs.powerfit.dtos.tickets.TicketDetalleDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.tickets.ITicketDetalleService;
import com.devs.powerfit.services.productos.ProductoService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.productoMapper.ProductoMapper;
import com.devs.powerfit.utils.mappers.ticketMappers.TicketDetalleMapper;
import com.devs.powerfit.utils.mappers.ticketMappers.TicketMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TicketDetalleService implements ITicketDetalleService {
    private final TicketDao ticketDao;
    private final TicketDetalleDao ticketDetalleDao;
    private final ProductoService productoService;
    private final TicketMapper ticketMapper;
    private final TicketDetalleMapper mapper;
    private final ProductoMapper productoMapper;

    @Autowired
    public TicketDetalleService(TicketDao ticketDao, TicketDetalleDao ticketDetalleDao, ProductoService productoService, TicketMapper ticketMapper, TicketDetalleMapper mapper, ProductoMapper productoMapper) {
        this.ticketDao = ticketDao;
        this.ticketDetalleDao = ticketDetalleDao;
        this.productoService = productoService;
        this.ticketMapper = ticketMapper;
        this.mapper = mapper;
        this.productoMapper = productoMapper;
    }

    @Override
    public TicketDetalleDto create(TicketDetalleDto ticketDetalleDto) {
        if (ticketDetalleDto.getTicketId() == null || ticketDetalleDto.getProductoId() == null || ticketDetalleDto.getPrecioUnitario() == null || ticketDetalleDto.getCantidad() == null) {
            throw new BadRequestException("Los campos ticketId, productoId , precioUnitario y cantidad son obligatorios para crear un nuevo detalle de ticket");
        }
        var ticketBean = ticketDao.findByIdAndActiveTrue(ticketDetalleDto.getTicketId());
        if (ticketBean.isEmpty()) {
            throw new NotFoundException("La ticket con ID " + ticketDetalleDto.getTicketId() + " no existe");
        }
        var productoDto = productoService.getById(ticketDetalleDto.getProductoId());
        if (productoDto == null) {
            throw new NotFoundException("El producto con ID " + ticketDetalleDto.getProductoId() + " no existe");
        }

        // Verificar si la cantidad disponible en el stock es suficiente
        if (!(productoDto.getCantidad() >= ticketDetalleDto.getCantidad())) {
            throw new BadRequestException("La cantidad que desea vender supera la disponible en el stock del producto");
        }

        // Actualizar el stock del producto
        productoDto.setCantidad(productoDto.getCantidad() - ticketDetalleDto.getCantidad());
        ProductoDto newProductoDto = productoService.update(productoDto.getId(), productoDto);

        // Calcular el subtotal y el total de IVA
        double subTotal = ticketDetalleDto.getPrecioUnitario() * ticketDetalleDto.getCantidad();
        double ivaTotal = subTotal * ticketDetalleDto.getIva();

        // Verificar si los datos coinciden con los proporcionados por el front
        if (ticketDetalleDto.getSubtotal() != subTotal || ticketDetalleDto.getIvaTotal() != ivaTotal) {
            throw new BadRequestException("Los datos no coinciden con los enviados por el frontend");
        }
        TicketDetalleBean ticketDetalleBean = new TicketDetalleBean();
        ticketDetalleBean.setActive(true);
        ticketDetalleBean.setTicket(ticketBean.get());
        ticketDetalleBean.setProducto(productoMapper.toBean(newProductoDto));
        ticketDetalleBean.setPrecioUnitario(ticketDetalleDto.getPrecioUnitario());
        ticketDetalleBean.setCantidad(ticketDetalleDto.getCantidad());
        ticketDetalleBean.setSubTotal(subTotal);
        ticketDetalleBean.setIva(ticketDetalleDto.getIva());
        ticketDetalleBean.setIvaTotal(ivaTotal);
        TicketDetalleBean savedTicketDetalle = ticketDetalleDao.save(ticketDetalleBean);
        return mapper.toDto(savedTicketDetalle);
    }

    @Override
    public TicketDetalleDto getById(Long id) {
        var ticket = ticketDetalleDao.findByIdAndActiveTrue(id);
        if (ticket.isPresent()) {
            return mapper.toDto(ticket.get());
        }
        throw new NotFoundException("Ticket Detalle no encontrada");
    }

    @Override
    public PageResponse<TicketDetalleDto> getAll(int page) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var ticketPage = ticketDetalleDao.findAllByActiveTrue(pageRequest);
        if (ticketPage.isEmpty()) {
            throw new NotFoundException("No hay tickets en la lista");
        }
        var ticketDtoPage = ticketPage.map(mapper::toDto);
        return new PageResponse<>(ticketDtoPage.getContent(),
                ticketDtoPage.getTotalPages(),
                ticketDtoPage.getTotalElements(),
                ticketDtoPage.getNumber() + 1);
    }

    @Override
    public TicketDetalleDto update(Long id, TicketDetalleDto ticketDetalleDto) {
        throw new BadRequestException("No se puede actualizar un ticket");
    }

    @Override
    public boolean delete(Long id) {return false;}
    @Override
    public List<TicketDetalleDto> getAllByTicketId(Long ticketId) {
        var ticketDetalleBeanList = ticketDetalleDao.findAllByTicketIdAndActiveTrue(ticketId);
        if (ticketDetalleBeanList.isEmpty()) {
            throw new NotFoundException("No hay tickets en la lista");
        }
        return ticketDetalleBeanList.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<TicketDetalleDto> getAllDetalles() {
        List<TicketDetalleBean> detalles = ticketDetalleDao.findAllByProductoIsNotNull();
        return detalles.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<TicketDetalleDto> getAllDetallesBetween(Date fechaInicio, Date fechaFin) {
        List<TicketBean> ticketsEnRango = ticketDao.findAllByFechaBetweenAndActiveTrue(fechaInicio, fechaFin);
        List<TicketDetalleDto> detalles = ticketsEnRango.stream()
                .map(ticket -> ticketDetalleDao.findAllByTicketIdAndProductoIsNotNullAndActiveTrue(ticket.getId()))
                .flatMap(List::stream)
                .map(mapper::toDto)
                .toList();
        return detalles;
    }
}
