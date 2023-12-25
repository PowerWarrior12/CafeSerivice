package com.simbirsoft.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtAuthenticationConverter jwtConverter,
            JwtDecoder jwtDecoder
    ) throws Exception {
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(httpRequests -> {
                    httpRequests
                            .requestMatchers("/api/auth/**").permitAll()
                            .requestMatchers("/api/admin/**").hasRole("ADMIN")
                            .requestMatchers("/api/basket/**", "/api/orders/**", "/api/products/**").authenticated()
                            .anyRequest().permitAll();
                })
                .oauth2ResourceServer(oauth2 -> { oauth2
                        .jwt(jwtConfigurer -> { jwtConfigurer
                                .jwtAuthenticationConverter(jwtConverter)
                                .decoder(jwtDecoder);
                    });
                })
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return PasswordEncoderFactories
                .createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider getauthenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }
}
