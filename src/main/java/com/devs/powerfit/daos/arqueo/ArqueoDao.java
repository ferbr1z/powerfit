package com.devs.powerfit.daos.arqueo;

import com.devs.powerfit.beans.arqueo.ArqueoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArqueoDao extends JpaRepository<ArqueoBean,Long> {
    Optional<ArqueoBean> findByIdAndActiveIsTrue(Long id);
    Page findAllByActiveIsTrue(Pageable pageable);
}
