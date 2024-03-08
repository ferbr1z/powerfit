package com.devs.powerfit.daos.productos;

import com.devs.powerfit.beans.productos.ProductoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoDao extends JpaRepository<ProductoBean,Long> {
    Optional<ProductoBean> findByNombreAndActiveIsTrue(String nombre);
    Optional<ProductoBean> findByCodigoAndActiveIsTrue(String codigo);
    Page findAllByActiveIsTrue(Pageable pageable);
}
