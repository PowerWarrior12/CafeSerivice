package com.simbirsoft.security;

import com.simbirsoft.common.TimeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;

import static com.simbirsoft.security.SecurityUtils.AUTHORITIES_KEY;
import static com.simbirsoft.security.SecurityUtils.JWT_ALGORITHM;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    @Value("${jwt.duration}")
    private Duration duration;
    @Value("${jwt.issuer}")
    private String issuer;
    private final TimeProvider timeProvider;
    private final JwtEncoder jwtEncoder;

    public String generateToken(Authentication authentication) {
        Instant startTime = timeProvider.getCurrentTimeOfCurrentTimeZone();
        Instant endTime = timeProvider.getTimeOfCurrentTimeZoneWithOffset(startTime, duration);

        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuedAt(startTime)
                .expiresAt(endTime)
                .issuer(issuer)
                .subject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claimsSet)).getTokenValue();
    }
}
