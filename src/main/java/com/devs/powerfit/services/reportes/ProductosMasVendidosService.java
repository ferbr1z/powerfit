package com.devs.powerfit.services.reportes;

import com.devs.powerfit.dtos.facturas.FacturaDetalleDto;
import com.devs.powerfit.dtos.productos.ProductoDto;
import com.devs.powerfit.dtos.reportes.ProductoMasVendidoDTO;
import com.devs.powerfit.interfaces.facturas.IFacturaDetalleService;
import com.devs.powerfit.interfaces.productos.IProductoService;
import com.devs.powerfit.interfaces.reportes.IProductosMasVendidosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
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

    // Método público para obtener los productos más vendidos
    @Override
    public List<ProductoMasVendidoDTO> productosMasVendidos() {
        // Obtener todos los detalles de factura
        List<FacturaDetalleDto> detallesFactura = facturaDetalleService.getAllDetalles();
        // Utilizar un método privado para procesar los detalles y obtener los productos más vendidos
        return getProductosMasVendidos(detallesFactura);
    }

    // Método público para obtener los productos más vendidos entre dos fechas
    @Override
    public List<ProductoMasVendidoDTO> productosMasVendidosBetween(Date fechaInicio, Date fechaFin) {
        // Obtener todos los detalles de factura entre las fechas dadas
        List<FacturaDetalleDto> detallesFactura = facturaDetalleService.getAllDetallesEntreFechas(fechaInicio, fechaFin);
        // Utilizar un método privado para procesar los detalles y obtener los productos más vendidos
        return getProductosMasVendidos(detallesFactura);
    }

    // Método privado para procesar los detalles de factura y obtener los productos más vendidos
    private List<ProductoMasVendidoDTO> getProductosMasVendidos(List<FacturaDetalleDto> detallesFactura) {
        // Mapear los detalles de factura a un mapa de productos y la cantidad vendida
        Map<Long, Integer> ventasPorProducto = mapearVentasPorProducto(detallesFactura);
        // Ordenar el mapa por la cantidad vendida en orden descendente
        List<Map.Entry<Long, Integer>> productosOrdenados = orderProductosByVentas(ventasPorProducto);
        // Limitar la lista a los primeros N productos más vendidos
        List<Map.Entry<Long, Integer>> productosMasVendidos = limitarProductosMasVendidos(productosOrdenados);
        // Obtener y retornar los detalles de los productos más vendidos
        return getDetallesMasVendidos(productosMasVendidos);
    }

    // Método privado para mapear los detalles de factura por producto y cantidad vendida
    private Map<Long, Integer> mapearVentasPorProducto(List<FacturaDetalleDto> detallesFactura) {
        return detallesFactura.stream()
                .collect(Collectors.groupingBy(FacturaDetalleDto::getProductoId,
                        Collectors.summingInt(FacturaDetalleDto::getCantidad)));
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
                    // Obtener el producto correspondiente al ID del mapa
                    ProductoDto producto = getProductoById(entry.getKey());
                    // Crear un DTO para el producto más vendido
                    ProductoMasVendidoDTO productoMasVendidoDTO = new ProductoMasVendidoDTO();
                    productoMasVendidoDTO.setNombreProducto(producto.getNombre()); // Establecer el nombre del producto
                    productoMasVendidoDTO.setCantidadVendida(entry.getValue()); // Establecer la cantidad vendida
                    return productoMasVendidoDTO; // Retornar el DTO del producto más vendido
                })
                .collect(Collectors.toList()); // Convertir a lista
    }

    // Método privado para obtener un producto por su ID
    private ProductoDto getProductoById(Long productoId) {
        return productoService.getById(productoId); // Utilizar el servicio de producto para obtener el producto por su ID
    }
}




