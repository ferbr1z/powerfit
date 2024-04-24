package com.devs.powerfit.daos.arqueo;

import com.devs.powerfit.beans.arqueo.ArqueoBean;
import com.devs.powerfit.beans.cajas.SesionCajaBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface ArqueoDao extends JpaRepository<ArqueoBean,Long> {
    Optional<ArqueoBean> findByIdAndActiveIsTrue(Long id);
    Page findAllByActiveIsTrue(Pageable pageable);

    @Query("SELECT a FROM ArqueoBean a WHERE a.fecha = ?1 AND a.active = true")
    Page findAllByFechaAndActiveTrue(Date fecha, Pageable pageable);

    Page findAllBySesionCajaAndActiveTrue(SesionCajaBean sesionCaja,Pageable pageable);
}
