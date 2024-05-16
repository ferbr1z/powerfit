package com.devs.powerfit.utils.specifications;

import com.devs.powerfit.beans.facturas.FacturaProveedorBean;
import com.devs.powerfit.dtos.facturas.FacturaFilterDTO;
import com.devs.powerfit.enums.EEstado;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class FacturaSpecification {
    public static Specification<FacturaProveedorBean> fromFiltro(FacturaFilterDTO filtro) {
        Specification<FacturaProveedorBean> spec = Specification.where(null);

        if (filtro.getEstadoPago() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("pagado"), filtro.getEstadoPago()));
        }

        if (filtro.getFecha() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("fecha"), filtro.getFecha()));
        }

        if (filtro.getValorDeuda() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("saldo"), filtro.getValorDeuda()));
        }

        if (filtro.getProveedor() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("nombreProveedor"), filtro.getProveedor()));
        }

        if (filtro.getRuc() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("rucProveedor"), filtro.getRuc()));
        }

        return spec;
    }
}
