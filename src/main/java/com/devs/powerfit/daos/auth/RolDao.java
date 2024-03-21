package com.devs.powerfit.daos.auth;

import com.devs.powerfit.beans.auth.RolBean;
import com.devs.powerfit.dtos.auth.RolDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolDao extends JpaRepository<RolBean, RolDto> {
    Optional<RolBean> findByIdAndActiveTrue(Long id);
}
