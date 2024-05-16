package com.devs.powerfit.interfaces.facturas;

import com.devs.powerfit.beans.facturas.FacturaProveedorBean;
import com.devs.powerfit.dtos.facturas.FacturaDto;
import com.devs.powerfit.dtos.facturas.FacturaProveedorDto;
import com.devs.powerfit.enums.EEstado;
import com.devs.powerfit.interfaces.bases.IService;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public interface IFacturaProveedorService extends IService<FacturaProveedorDto> {
    PageResponse<FacturaProveedorDto> searchByNombreProveedor(String nombre, int page);
    PageResponse<FacturaProveedorDto> searchByRucProveedor(String nombre, int page);
    FacturaProveedorDto searchByNumeroFactura(String numeroFactura);

    public PageResponse<FacturaProveedorDto> filtrarFacturas(Specification<FacturaProveedorBean> spec, int page);


}
