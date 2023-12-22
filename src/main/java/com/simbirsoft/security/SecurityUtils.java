package com.simbirsoft.security;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;

public class SecurityUtils {
    public final static String AUTHORITIES_KEY = "auth";
    public final static MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;
}
