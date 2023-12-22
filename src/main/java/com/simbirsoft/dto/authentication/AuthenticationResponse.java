package com.simbirsoft.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.simbirsoft.dto.user.UserResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthenticationResponse {
    @JsonProperty("token")
    private final String token;
    @JsonProperty("user")
    private final UserResponse user;
}
