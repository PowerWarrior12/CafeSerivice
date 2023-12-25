package com.simbirsoft.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RegistrationResponse {
    @JsonProperty("message")
    private final String message;
}
