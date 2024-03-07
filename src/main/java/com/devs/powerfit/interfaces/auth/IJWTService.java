package com.devs.powerfit.interfaces.auth;

import org.springframework.security.core.userdetails.UserDetails;

public interface IJWTService {
    String extractUsername(String token);
    String generateToken(UserDetails userDetails);
    boolean isTokenValid(String token, UserDetails userDetails);
}
