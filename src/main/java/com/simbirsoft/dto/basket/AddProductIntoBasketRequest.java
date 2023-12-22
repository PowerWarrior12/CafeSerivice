package com.simbirsoft.dto.basket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AddProductIntoBasketRequest {
    private final int count;
    private final int productId;

    public AddProductIntoBasketRequest(
            @JsonProperty("count") int count,
            @JsonProperty("product_id") int productId) {
        this.count = count;
        this.productId = productId;
    }
}
