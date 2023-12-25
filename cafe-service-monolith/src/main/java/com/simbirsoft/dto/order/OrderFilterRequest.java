package com.simbirsoft.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class OrderFilterRequest {
    @JsonProperty("logins")
    private final List<String> customersLogins;
}
