package com.devs.powerfit.daos.proveedores;

import com.devs.powerfit.beans.proveedores.ProveedorBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProveedorDao extends JpaRepository<ProveedorBean, Long> {
    Optional<ProveedorBean> findByIdAndActiveIsTrue(Long id);
    Optional<ProveedorBean> findByEmailAndActiveIsTrue(String email);
    Optional<ProveedorBean> findByRucAndActiveIsTrue(String ruc);
    Optional<ProveedorBean> findByTelefonoAndActiveIsTrue(String telefono);
    Page findAllByActiveIsTrue(Pageable pageable);
    Page findAllByNombreContainingIgnoreCaseAndActiveIsTrue(Pageable pageable, String nombre);

    boolean existsByNombreAndIdNot(String nombre, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByRucAndIdNot(String ruc, Long id);

    boolean existsByTelefonoAndIdNot(String telefono, Long id);



}

