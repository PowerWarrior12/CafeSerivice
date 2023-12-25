package com.simbirsoft.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserResponse {
    @JsonProperty("login")
    private final String login;
    @JsonProperty("first_name")
    private final String firstName;
    @JsonProperty("last_name")
    private final String lastName;
    @JsonProperty("address")
    private final String address;
    @JsonProperty("phone_number")
    private final String phoneNumber;
}
