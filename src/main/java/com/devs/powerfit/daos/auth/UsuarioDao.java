package com.devs.powerfit.daos.auth;

import com.devs.powerfit.beans.auth.UsuarioBean;
import com.devs.powerfit.dtos.auth.UsuarioDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioDao extends JpaRepository<UsuarioBean, UsuarioDto> {
    Optional<UsuarioBean> findByEmailAndActiveIsTrue(String email);
}
