package com.devs.powerfit.services.reportes.productos;

import com.devs.powerfit.beans.productos.ProductoBean;
import com.devs.powerfit.daos.productos.ProductoDao;
import com.devs.powerfit.dtos.facturas.FacturaDetalleDto;
import com.devs.powerfit.dtos.productos.ProductoDto;
import com.devs.powerfit.dtos.reportes.productos.ProductoMasVendidoDTO;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.facturas.IFacturaDetalleService;
import com.devs.powerfit.interfaces.productos.IProductoService;
import com.devs.powerfit.interfaces.reportes.productos.IProductoReportesService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.productoMapper.ProductoMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReporteProductoService implements IProductoReportesService {

    private final IFacturaDetalleService facturaDetalleService;
    private final IProductoService productoService;
    private final ProductoDao productoDao;
    private final ProductoMapper mapper;

    @Autowired
    public ReporteProductoService(IFacturaDetalleService facturaDetalleService, IProductoService productoService, ProductoDao productoDao, ProductoMapper mapper) {
        this.facturaDetalleService = facturaDetalleService;
        this.productoService = productoService;
        this.productoDao = productoDao;
        this.mapper = mapper;
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

    @Override
    public PageResponse<ProductoDto> getAll(int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        Page productos = productoDao.findAllByCantidadAndActiveIsTrue(pag, 0);
        if (productos.isEmpty()){
            throw new NotFoundException("No hay productos sin Stock");
        }
        var productosDto = productos.map(producto ->
                mapper.toDto((ProductoBean) producto));
        return new PageResponse<ProductoDto>(productosDto.getContent(),
                productosDto.getTotalPages(),
                productosDto.getTotalElements(),
                productosDto.getNumber() + 1);
    }

    @Override
    public Long getCantidadSinStock() {
        return productoDao.countByCantidadAndActiveIsTrue(0);
    }

    // Método privado para mapear los detalles de factura y ticket por producto y cantidad vendida
    private Map<Long, Integer> mapearVentasPorProducto(List<FacturaDetalleDto> detallesFactura) {
        return  detallesFactura.stream()
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
