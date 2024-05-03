package com.devs.powerfit.daos.empleados;

import com.devs.powerfit.beans.empleados.EmpleadoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoDao extends JpaRepository<EmpleadoBean, Long> {
    Optional<EmpleadoBean> findByIdAndActiveIsTrue(Long id);
    Optional<EmpleadoBean> findByEmailAndActiveIsTrue(String email);
    Optional<EmpleadoBean> findByCedulaAndActiveIsTrue(String email);
    Page<EmpleadoBean> findAllByActiveIsTrue(Pageable pageable);
    Page findAllByNombreContainingIgnoreCaseAndActiveIsTrue(Pageable pageable, String nombre);

    Page<EmpleadoBean> findAllByRolAndActiveIsTrue(Pageable pageable, Long rolId);

    Page<EmpleadoBean> findAllByRolAndNombreContainingIgnoreCaseAndActiveIsTrue(Pageable pageable, Long rolId, String nombre);

}
