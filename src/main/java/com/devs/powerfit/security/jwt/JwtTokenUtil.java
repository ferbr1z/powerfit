package com.devs.powerfit.security.jwt;

import com.devs.powerfit.beans.UsuarioBean;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
    @Value("${app.jwt.expiration.duration}")
    private Long EXPIRE_DURATION;
    @Value("${app.jwt.secretkey}")
    private String SECRET_KEY;

    public String generateAccesToken(UsuarioBean user){
        return Jwts.builder()
                .setSubject(String.format("%s,%s", user.getId(), user.getEmail()))
                .setIssuer("sistemassd")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .compact();
    }


    public boolean validateAccesToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return true; // Si no se lanzan excepciones, el token es válido.
        } catch (ExpiredJwtException e) {
            LOGGER.error("Token expirado", e);
        } catch (MalformedJwtException e) {
            LOGGER.error("Token mal formado", e);
        } catch (Exception e) {
            LOGGER.error("Error al validar el token", e);
        }

        return false;
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }


    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
