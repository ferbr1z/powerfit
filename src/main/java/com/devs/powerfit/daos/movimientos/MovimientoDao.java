package com.devs.powerfit.daos.movimientos;

import com.devs.powerfit.beans.cajas.SesionCajaBean;
import com.devs.powerfit.beans.facturas.FacturaBean;
import com.devs.powerfit.beans.facturas.FacturaProveedorBean;
import com.devs.powerfit.beans.movimientos.MovimientoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovimientoDao extends JpaRepository<MovimientoBean,Long> {
    Optional<MovimientoBean> findByIdAndActiveTrue(Long id);
    Page<MovimientoBean> findAllByActiveTrue(Pageable pageable);
    List<MovimientoBean> findAllBySesionAndActiveTrue(SesionCajaBean sesion);
    Page<MovimientoBean> findAllBySesionAndActiveTrue(Pageable pageable,SesionCajaBean sesion);
    Page<MovimientoBean> findAllByFechaBetween(Pageable pageable,Date fechaInferior,Date fechaSuperior);
    List<MovimientoBean> findAllByEntradaAndActiveTrue(boolean entrada);
    Page<MovimientoBean> findAllByEntradaAndActiveTrue(Pageable pageable,boolean entrada);
    Page<MovimientoBean>findAllBySesionAndEntradaAndActiveTrue(Pageable pageable, SesionCajaBean sesion, boolean entrada);
    List<MovimientoBean> findAllBySesionAndEntradaAndActiveTrue(SesionCajaBean sesion,boolean entrada);
    List<MovimientoBean> findAllByFacturaAndActiveTrue(FacturaBean factura);
    Page<MovimientoBean> findAllByFacturaAndActiveTrue(Pageable pageable, FacturaBean factura);
    List<MovimientoBean> findAllByFacturaProveedorAndActiveTrue(FacturaProveedorBean facturaProveedor);
    Page<MovimientoBean> findAllByFacturaProveedorAndActiveTrue(Pageable pageable, FacturaProveedorBean facturaProveedor);
    Page<MovimientoBean> findAllByFacturaIsNotNull(Pageable pageable);
    Page<MovimientoBean> findAllByFacturaProveedorIsNotNull(Pageable pageable);
    Page<MovimientoBean> findAllByComprobanteNumero(Pageable pageable,String numero);
    Page<MovimientoBean> findAllByComprobanteNombreContainingIgnoreCase(Pageable pageable,String nombre);
    Page<MovimientoBean> findAllByComprobanteNombreContainingIgnoreCaseOrEntrada(Pageable pageable,String nombre,Boolean entrada);
    Page<MovimientoBean> findAllByComprobanteNombreContainingIgnoreCaseAndFacturaIsNotNullAndEntradaAndFechaBetweenAndNombreCajaContainingIgnoreCase(Pageable pageable, String nombre, Boolean entrada, LocalDate fechaInicio,LocalDate fechaFin,String nombreCaja);

    List<MovimientoBean> findAllByFechaBetweenAndEntradaTrue(LocalDate fechaInicio,LocalDate fechaFin);
    Page<MovimientoBean> findAll(Specification<MovimientoBean> movimientoBeanSpecification, Pageable pageable);
}
