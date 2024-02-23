package com.devs.powerfit.daos;

import com.devs.powerfit.beans.ActividadBean;
import com.devs.powerfit.dtos.ActividadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ActividadDao extends JpaRepository<ActividadBean, ActividadDto> {
    Page<ActividadBean> findByNombreAndActiveIsTrue(Pageable pageable, String nombre);
    Optional<ActividadBean> findByIdAndActiveTrue(Long id);
    Page<ActividadBean> findAllByActiveTrue(Pageable pageable);
}
