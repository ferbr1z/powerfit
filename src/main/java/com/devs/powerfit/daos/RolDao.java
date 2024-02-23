package com.devs.powerfit.daos;

import com.devs.powerfit.beans.RolBean;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolDao extends JpaRepository<RolBean, Long> {
    Optional<RolBean> findByIdAndActiveTrue(Long id);
}
