package com.simbirsoft.dto.basket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class BasketResponse {
    @JsonProperty("items")
    private final List<BasketItemResponse> basketItemResponseList;
}
