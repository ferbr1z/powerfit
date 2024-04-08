package com.devs.powerfit.services.reportes;

import com.devs.powerfit.beans.tickets.TicketDetalleBean;
import com.devs.powerfit.daos.tickets.TicketDetalleDao;
import com.devs.powerfit.dtos.reportes.ProductoMasVendidoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductoMasVendido {

    private final TicketDetalleDao ticketDetalleDao;

    @Autowired
    public ProductoMasVendido(TicketDetalleDao ticketDetalleDao) {
        this.ticketDetalleDao = ticketDetalleDao;
    }

    public List<ProductoMasVendidoDTO> generarReporteProductosMasVendidos() {
        // Obtener todos los detalles de los tickets
        List<TicketDetalleBean> detallesTickets = ticketDetalleDao.findAllByActiveIsTrue();

        // Agrupar los detalles por producto y sumar las cantidades vendidas
        Map<String, Integer> mapProductosMasVendidos = detallesTickets.stream()
                .collect(Collectors.groupingBy(
                        detalle -> detalle.getProducto().getNombre(),
                        Collectors.summingInt(TicketDetalleBean::getCantidad)
                ));

        // Convertir el mapa en una lista de DTOs
        return mapProductosMasVendidos.entrySet().stream()
                .map(entry -> new ProductoMasVendidoDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
