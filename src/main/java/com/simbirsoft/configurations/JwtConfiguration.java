package com.simbirsoft.configurations;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.simbirsoft.security.LoggingJwtDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import static com.simbirsoft.security.SecurityUtils.AUTHORITIES_KEY;
import static com.simbirsoft.security.SecurityUtils.JWT_ALGORITHM;

@Configuration
public class JwtConfiguration {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.
                withSecretKey(getSecretKey()).
                macAlgorithm(JWT_ALGORITHM)
                .build();
        return new LoggingJwtDecoder(jwtDecoder);
    }
    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        grantedAuthoritiesConverter.setAuthoritiesClaimName(AUTHORITIES_KEY);

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
    private SecretKey getSecretKey() {
        byte[] bytesSecret = Base64.getDecoder().decode(jwtSecret);
        return new SecretKeySpec(bytesSecret, 0, bytesSecret.length, JWT_ALGORITHM.getName());
    }
}
