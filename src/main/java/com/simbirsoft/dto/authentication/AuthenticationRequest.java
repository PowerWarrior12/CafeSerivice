package com.simbirsoft.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AuthenticationRequest {
    private final String login;
    private final String password;

    public AuthenticationRequest(
            @JsonProperty("login") String login,
            @JsonProperty("password") String password) {
        this.login = login;
        this.password = password;
    }
}
