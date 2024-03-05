package com.devs.powerfit.daos.actividades;

import com.devs.powerfit.beans.actividades.ActividadBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ActividadDao extends JpaRepository<ActividadBean, Long> {
    Page<ActividadBean> findByNombreContainingIgnoreCaseAndActiveIsTrue(Pageable pageable, String nombre);
    Optional<ActividadBean> findByIdAndActiveTrue(Long id);
    Page<ActividadBean> findAllByActiveTrue(Pageable pageable);
}
