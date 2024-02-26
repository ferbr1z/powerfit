package com.devs.powerfit.daos;

import com.devs.powerfit.beans.UsuarioBean;
import com.devs.powerfit.dtos.UsuarioDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioDao extends JpaRepository<UsuarioBean, UsuarioDto> {
    Optional<UsuarioBean> findByEmailAndActiveIsTrue(String email);
}
