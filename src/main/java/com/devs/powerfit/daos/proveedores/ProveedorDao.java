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

    Page findAllByActiveIsTrue(Pageable pageable);
}

