package com.devs.powerfit.utils.specifications;

import com.devs.powerfit.beans.facturas.FacturaProveedorBean;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ReporteCompraSpecification {
    public static Specification<FacturaProveedorBean> hasNumeroFactura(String numeroFactura) {
        return (root, query, builder) ->
                builder.equal(builder.lower(root.get("nroFactura")), numeroFactura.toLowerCase());
    }

    public static Specification<FacturaProveedorBean> hasFechaBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        return (root, query, builder) ->
                builder.between(root.get("fecha"), fechaInicio, fechaFin);
    }

    public static Specification<FacturaProveedorBean> hasNombreProveedor(String nombreProveedor) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("nombreProveedor")), "%" + nombreProveedor.toLowerCase() + "%");
    }

    public static Specification<FacturaProveedorBean> hasRucProveedor(String rucProveedor) {
        return (root, query, builder) ->
                builder.equal(builder.lower(root.get("rucProveedor")), rucProveedor.toLowerCase());
    }

    public static Specification<FacturaProveedorBean> isPagado(Boolean pagado) {
        return (root, query, builder) ->
                builder.equal(root.get("pagado"), pagado);
    }



    public static Specification<FacturaProveedorBean> isActive() {
        return (root, query, builder) ->
                builder.isTrue(root.get("active"));
    }
}
