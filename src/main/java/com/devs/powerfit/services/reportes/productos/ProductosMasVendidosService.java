package com.devs.powerfit.services.reportes.productos;

import com.devs.powerfit.dtos.facturas.FacturaDetalleDto;
import com.devs.powerfit.dtos.productos.ProductoDto;
import com.devs.powerfit.dtos.reportes.ProductoMasVendidoDTO;
import com.devs.powerfit.interfaces.facturas.IFacturaDetalleService;
import com.devs.powerfit.interfaces.productos.IProductoService;
import com.devs.powerfit.interfaces.reportes.productos.IProductosMasVendidosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductosMasVendidosService implements IProductosMasVendidosService {

    private final IFacturaDetalleService facturaDetalleService;
    private final IProductoService productoService;

    @Autowired
    public ProductosMasVendidosService(IFacturaDetalleService facturaDetalleService, IProductoService productoService) {
        this.facturaDetalleService = facturaDetalleService;
        this.productoService = productoService;
    }


    @Override
    public List<ProductoMasVendidoDTO> productosMasVendidosBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        // Validación de las fechas
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        List<FacturaDetalleDto> detallesFactura = facturaDetalleService.getAllDetallesBetween(fechaInicio, fechaFin);

        return obtenerProductosMasVendidos(detallesFactura);
    }

    @Override
    public List<ProductoMasVendidoDTO> productosMasVendidosTotal() {
        List<FacturaDetalleDto> detallesFactura = facturaDetalleService.getAllDetalles();

        return obtenerProductosMasVendidos(detallesFactura);
    }

    @Override
    public List<ProductoMasVendidoDTO> productosMasVendidosActual() {
        // Establecer la fechaFin como la fecha actual
        LocalDate fechaFin = LocalDate.now();
        LocalDate fechaInicio = LocalDate.now().minusDays(7);
        return productosMasVendidosBetween(fechaInicio,fechaFin);
    }

    // Método privado para mapear los detalles de factura y ticket por producto y cantidad vendida
    private Map<Long, Integer> mapearVentasPorProducto(List<FacturaDetalleDto> detallesFactura) {
        Map<Long, Integer> ventasPorProducto = new HashMap<>();

        // Mapear detalles de factura
        detallesFactura.forEach(detalleFactura ->
                ventasPorProducto.put(detalleFactura.getProductoId(),
                        ventasPorProducto.getOrDefault(detalleFactura.getProductoId(), 0) + detalleFactura.getCantidad()));



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
    private List<ProductoMasVendidoDTO> obtenerProductosMasVendidos(List<FacturaDetalleDto> detallesFactura) {
        Map<Long, Integer> ventasPorProducto = mapearVentasPorProducto(detallesFactura);
        List<Map.Entry<Long, Integer>> productosOrdenados = orderProductosByVentas(ventasPorProducto);
        List<Map.Entry<Long, Integer>> productosMasVendidos = limitarProductosMasVendidos(productosOrdenados);

        return getDetallesMasVendidos(productosMasVendidos);
    }
}
