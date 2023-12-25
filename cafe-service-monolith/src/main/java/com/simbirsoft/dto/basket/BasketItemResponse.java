package com.simbirsoft.dto.basket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.simbirsoft.dto.product.ProductResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BasketItemResponse {
    @JsonProperty("id")
    private final int id;
    @JsonProperty("product")
    private final ProductResponse productResponse;
    @JsonProperty("count")
    private final int count;
}
