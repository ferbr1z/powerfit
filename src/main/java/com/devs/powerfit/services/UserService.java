package com.devs.powerfit.services;

import com.devs.powerfit.daos.UsuarioDao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UsuarioDao userRepository;

    public UserDetailsService userDetailsService() {
        return userEmail -> userRepository.findByEmailAndActiveIsTrue(userEmail).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
