package com.devs.powerfit.daos;

import com.devs.powerfit.beans.UsuarioBean;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioDao extends JpaRepository<UsuarioBean, Long> {
    Optional<UsuarioBean> findByEmailAndActiveIsTrue(String email);
}
