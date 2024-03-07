package com.devs.powerfit.daos.suscripciones;

import com.devs.powerfit.beans.suscripciones.SuscripcionDetalleBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SuscripcionDetalleDao extends JpaRepository<SuscripcionDetalleBean,Long>{
    Optional<SuscripcionDetalleBean> findByIdAndActiveTrue(Long id);
    Page<SuscripcionDetalleBean> findAllByActiveTrue(Pageable pageable);
    List<SuscripcionDetalleBean> findAllBySuscripcionIdAndActiveTrue(Long suscripcionId);

}
