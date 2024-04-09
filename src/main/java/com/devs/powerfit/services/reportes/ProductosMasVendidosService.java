package com.devs.powerfit.services.reportes;

import com.devs.powerfit.dtos.facturas.FacturaDetalleDto;
import com.devs.powerfit.dtos.productos.ProductoDto;
import com.devs.powerfit.dtos.reportes.ProductoMasVendidoDTO;
import com.devs.powerfit.dtos.tickets.TicketDetalleDto;
import com.devs.powerfit.interfaces.facturas.IFacturaDetalleService;
import com.devs.powerfit.interfaces.productos.IProductoService;
import com.devs.powerfit.interfaces.reportes.IProductosMasVendidosService;
import com.devs.powerfit.interfaces.tickets.ITicketDetalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductosMasVendidosService implements IProductosMasVendidosService {

    private final IFacturaDetalleService facturaDetalleService;
    private final IProductoService productoService;
    private final ITicketDetalleService ticketDetalleService;

    @Autowired
    public ProductosMasVendidosService(IFacturaDetalleService facturaDetalleService, IProductoService productoService,ITicketDetalleService ticketDetalleService) {
        this.facturaDetalleService = facturaDetalleService;
        this.productoService = productoService;
        this.ticketDetalleService = ticketDetalleService;
    }


    @Override
    public List<ProductoMasVendidoDTO> productosMasVendidosBetween(Date fechaInicio, Date fechaFin) {
        // Validación de las fechas
        if (fechaInicio.after(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        List<FacturaDetalleDto> detallesFactura = facturaDetalleService.getAllDetallesBetween(fechaInicio, fechaFin);
        List<TicketDetalleDto> detallesTicket = ticketDetalleService.getAllDetallesBetween(fechaInicio, fechaFin);

        return obtenerProductosMasVendidos(detallesFactura, detallesTicket);
    }

    @Override
    public List<ProductoMasVendidoDTO> productosMasVendidosTotal() {
        List<FacturaDetalleDto> detallesFactura = facturaDetalleService.getAllDetalles();
        List<TicketDetalleDto> detallesTicket = ticketDetalleService.getAllDetalles();

        return obtenerProductosMasVendidos(detallesFactura, detallesTicket);
    }

    @Override
    public List<ProductoMasVendidoDTO> productosMasVendidosActual() {
        // Establecer la fechaFin como la fecha actual
        Calendar calendar = Calendar.getInstance();
        Date fechaFin = calendar.getTime();
        // Establecer la fechaInicio como una semana antes de la fecha actual
        calendar.add(Calendar.DATE, -7);
        Date fechaInicio = calendar.getTime();
        return productosMasVendidosBetween(fechaInicio,fechaFin);
    }

    // Método privado para mapear los detalles de factura y ticket por producto y cantidad vendida
    private Map<Long, Integer> mapearVentasPorProducto(List<FacturaDetalleDto> detallesFactura, List<TicketDetalleDto> detallesTicket) {
        Map<Long, Integer> ventasPorProducto = new HashMap<>();

        // Mapear detalles de factura
        detallesFactura.forEach(detalleFactura ->
                ventasPorProducto.put(detalleFactura.getProductoId(),
                        ventasPorProducto.getOrDefault(detalleFactura.getProductoId(), 0) + detalleFactura.getCantidad()));

        // Mapear detalles de ticket
        detallesTicket.forEach(detalleTicket ->
                ventasPorProducto.put(detalleTicket.getProductoId(),
                        ventasPorProducto.getOrDefault(detalleTicket.getProductoId(), 0) + detalleTicket.getCantidad()));


        return ventasPorProducto;
    }

    // Método privado para ordenar los productos por cantidad vendida
    private List<Map.Entry<Long, Integer>> orderProductosByVentas(Map<Long, Integer> ventasPorProducto) {
        return ventasPorProducto.entrySet().stream()
                .sorted((p1, p2) -> p2.getValue().compareTo(p1.getValue()))
                .toList();
    }

    // Método privado para limitar la lista de productos a los más vendidos
    private List<Map.Entry<Long, Integer>> limitarProductosMasVendidos(List<Map.Entry<Long, Integer>> productosOrdenados) {
        int numProductosMostrados = 5; // Número de productos más vendidos a mostrar
        return productosOrdenados.stream()
                .limit(numProductosMostrados)
                .collect(Collectors.toList());
    }

    // Método privado para obtener los detalles de los productos más vendidos
    private List<ProductoMasVendidoDTO> getDetallesMasVendidos(List<Map.Entry<Long, Integer>> productosMasVendidos) {
        return productosMasVendidos.stream()
                .map(entry -> {
                    ProductoDto producto = getProductoById(entry.getKey());
                    ProductoMasVendidoDTO productoMasVendidoDTO = new ProductoMasVendidoDTO();
                    productoMasVendidoDTO.setNombreProducto(producto.getNombre());
                    productoMasVendidoDTO.setCantidadVendida(entry.getValue());
                    return productoMasVendidoDTO;
                })
                .collect(Collectors.toList());
    }

    // Método privado para obtener un producto por su ID
    private ProductoDto getProductoById(Long productoId) {
        return productoService.getById(productoId); // Utilizar el servicio de producto para obtener el producto por su ID
    }


    // Método que encapsula la lógica común entre productosMasVendidosBetween y productosMasVendidosTotal
    private List<ProductoMasVendidoDTO> obtenerProductosMasVendidos(List<FacturaDetalleDto> detallesFactura, List<TicketDetalleDto> detallesTicket) {
        Map<Long, Integer> ventasPorProducto = mapearVentasPorProducto(detallesFactura, detallesTicket);
        List<Map.Entry<Long, Integer>> productosOrdenados = orderProductosByVentas(ventasPorProducto);
        List<Map.Entry<Long, Integer>> productosMasVendidos = limitarProductosMasVendidos(productosOrdenados);

        return getDetallesMasVendidos(productosMasVendidos);
    }
}
