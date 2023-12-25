package com.simbirsoft.services.mappers.authentication;

import com.simbirsoft.domain.User;
import com.simbirsoft.dto.authentication.AuthenticationRequest;
import com.simbirsoft.dto.authentication.AuthenticationResponse;
import com.simbirsoft.services.mappers.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationMapper {
    private final UserMapper userResponseToUserMapper;
    public Authentication authenticationRequestToAuthentication(AuthenticationRequest request) {
        return new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword());
    }
    public AuthenticationResponse getAuthenticationResponse(String token, User user) {
        return new AuthenticationResponse(token, userResponseToUserMapper.userToUserResponse(user));
    }
    public UserDetails userToUserDetails(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.name()))
                        .toList()
        );
    }
}
