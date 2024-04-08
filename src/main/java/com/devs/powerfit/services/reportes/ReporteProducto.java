//package com.devs.powerfit.services.reportes;
//
//import com.devs.powerfit.beans.facturas.FacturaDetalleBean;
//import com.devs.powerfit.beans.tickets.TicketDetalleBean;
//import com.devs.powerfit.daos.facturas.FacturaDetalleDao;
//import com.devs.powerfit.dtos.reportes.ProductoMasVendidoDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Service
//@Transactional
//public class ReporteProducto {
//    @Autowired
//    public ReporteProducto(FacturaDetalleDao facturaDetalleDao) {
//        this.facturaDetalleDao = facturaDetalleDao;
//    }
//
//    public List<ProductoMasVendidoDTO> generarReporteProductosMasVendidos() {
//        // Obtener todos los detalles de los tickets
//        List<FacturaDetalleBean> detallesTickets = facturaDetalleDao.findAllByActiveIsTrue();
//
//        // Agrupar los detalles por producto y sumar las cantidades vendidas
//        Map<Long, Integer> mapProductosMasVendidos = detallesTickets.stream()
//                .collect(Collectors.groupingBy(
//                        detalle -> detalle.getProducto().getId(),
//                        Collectors.summingInt(FacturaDetalleBean::getCantidad)
//                        ));
//
//        // Convertir el mapa en una lista de DTOs
//        return mapProductosMasVendidos.entrySet().stream()
//                .map(entry -> new ProductoMasVendidoDTO(entry.getKey(), entry.getValue()))
//                .collect(Collectors.toList());
//    }
//
//    private final FacturaDetalleDao facturaDetalleDao;
//
//
//}
