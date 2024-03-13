package com.devs.powerfit.daos.empleados;

import com.devs.powerfit.beans.empleados.EmpleadoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpleadoDao extends JpaRepository<EmpleadoBean, Long> {
    Optional<EmpleadoBean> findByIdAndActiveIsTrue(Long id);
    Optional<EmpleadoBean> findByEmailAndActiveIsTrue(String email);
    Optional<EmpleadoBean> findByCedulaAndActiveIsTrue(String email);
    Page<EmpleadoBean> findAllByActiveIsTrue(Pageable pageable);
}
