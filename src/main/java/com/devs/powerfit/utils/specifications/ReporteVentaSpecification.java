package com.devs.powerfit.utils.specifications;

import com.devs.powerfit.beans.facturas.FacturaBean;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ReporteVentaSpecification {
    public static Specification<FacturaBean> hasNumeroFactura(String numeroFactura) {
        return (root, query, builder) ->
                builder.equal(builder.lower(root.get("nroFactura")), numeroFactura.toLowerCase());
    }

    public static Specification<FacturaBean> hasFechaBetween(LocalDate fechaInicio, LocalDate fechaFin) {
        return (root, query, builder) ->
                builder.between(root.get("fecha"), fechaInicio, fechaFin);
    }

    public static Specification<FacturaBean> hasNombreCliente(String nombreCliente) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("nombreCliente")), "%" + nombreCliente.toLowerCase() + "%");
    }

    public static Specification<FacturaBean> hasRucCliente(String rucCliente) {
        return (root, query, builder) ->
                builder.equal(builder.lower(root.get("rucCliente")), rucCliente.toLowerCase());
    }

    public static Specification<FacturaBean> isPagado(Boolean pagado) {
        return (root, query, builder) ->
                builder.equal(root.get("pagado"), pagado);
    }

    public static Specification<FacturaBean> hasNombreEmpleado(String nombreEmpleado) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("nombreEmpleado")), "%" + nombreEmpleado.toLowerCase() + "%");
    }

    public static Specification<FacturaBean> isActive() {
        return (root, query, builder) ->
                builder.isTrue(root.get("active"));
    }
}
