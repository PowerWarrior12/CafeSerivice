package com.simbirsoft.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

@RequiredArgsConstructor
@Log4j2
public class LoggingJwtDecoder implements JwtDecoder {
    private final JwtDecoder jwtDecoder;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            return jwtDecoder.decode(token);
        } catch (Exception e) {
            log.log(Level.INFO, String.format("Problems with decode jwt: %s", token));
            throw e;
        }
    }
}
