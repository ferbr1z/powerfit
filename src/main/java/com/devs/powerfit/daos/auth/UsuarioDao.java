package com.devs.powerfit.daos.auth;

import com.devs.powerfit.beans.auth.UsuarioBean;
import com.devs.powerfit.dtos.auth.UsuarioDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UsuarioDao extends JpaRepository<UsuarioBean, UsuarioDto> {
    Optional<UsuarioBean> findByEmailAndActiveIsTrue(String email);
    Optional<UsuarioBean> findByNombreAndActiveIsTrue(String nombre);
    Optional<UsuarioBean> findByIdAndActiveTrue(Long id);
}
