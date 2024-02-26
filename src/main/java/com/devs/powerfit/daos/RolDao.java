package com.devs.powerfit.daos;

import com.devs.powerfit.beans.RolBean;
import com.devs.powerfit.dtos.RolDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolDao extends JpaRepository<RolBean, RolDto> {
    Optional<RolBean> findByIdAndActiveTrue(Long id);
}
