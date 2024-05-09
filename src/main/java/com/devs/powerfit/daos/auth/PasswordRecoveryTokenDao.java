package com.devs.powerfit.daos.auth;

import com.devs.powerfit.beans.auth.PasswordRecoveryTokenBean;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordRecoveryTokenDao extends JpaRepository<PasswordRecoveryTokenBean, Long> {
    Optional<PasswordRecoveryTokenBean> findByTokenAndActiveIsTrue(String token);
}
