package com.devs.powerfit.daos.cajas;

import com.devs.powerfit.beans.cajas.CajaBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CajaDao extends JpaRepository<CajaBean,Long> {
    boolean existsByNombreAndActiveTrue(String nombre);

    Optional<CajaBean> findByIdAndActiveTrue(Long id);
    Page<CajaBean> findAllByActiveTrue(Pageable pageable);
    List<CajaBean> findAllByActiveTrue();
    Optional<CajaBean> findFirstByOrderByNumeroCajaDesc();
    Page<CajaBean>findAllByNombreContainingIgnoreCaseAndActiveTrue(Pageable pageable,String nombre);
    Long countByActiveTrue();
}
