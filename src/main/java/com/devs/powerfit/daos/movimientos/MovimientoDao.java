package com.devs.powerfit.daos.movimientos;

import com.devs.powerfit.beans.cajas.SesionCajaBean;
import com.devs.powerfit.beans.facturas.FacturaBean;
import com.devs.powerfit.beans.facturas.FacturaProveedorBean;
import com.devs.powerfit.beans.movimientos.MovimientoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovimientoDao extends JpaRepository<MovimientoBean,Long> {
    Optional<MovimientoBean> findByIdAndActiveTrue(Long id);
    Page<MovimientoBean> findAllByActiveTrue(Pageable pageable);
    List<MovimientoBean> findAllBySesionAndActiveTrue(SesionCajaBean sesion);
    Page<MovimientoBean> findAllBySesionAndActiveTrue(Pageable pageable,SesionCajaBean sesion);
    Page<MovimientoBean> findAllByFechaBetweenAndActive(Pageable pageable,Date fechaMenor,Date fechaMayor);
    List<MovimientoBean> findAllByFechaBetweenAndActive(Date fechaMenor,Date fechaMayor);
    List<MovimientoBean> findAllByEntradaAndActiveTrue(boolean entrada);
    Page<MovimientoBean> findAllByEntradaAndActiveTrue(Pageable pageable,boolean entrada);
    Page<MovimientoBean>findAllBySesionAndEntradaAndActiveTrue(Pageable pageable, SesionCajaBean sesion, boolean entrada);
    List<MovimientoBean> findAllBySesionAndEntradaAndActiveTrue(SesionCajaBean sesion,boolean entrada);
    List<MovimientoBean> findAllByFacturaAndActiveTrue(FacturaBean factura);
    Page<MovimientoBean> findAllByFacturaAndActiveTrue(Pageable pageable, FacturaBean factura);
    List<MovimientoBean> findAllByFacturaProveedorAndActiveTrue(FacturaProveedorBean facturaProveedor);
    Page<MovimientoBean> findAllByFacturaProveedorAndActiveTrue(Pageable pageable, FacturaProveedorBean facturaProveedor);
}
