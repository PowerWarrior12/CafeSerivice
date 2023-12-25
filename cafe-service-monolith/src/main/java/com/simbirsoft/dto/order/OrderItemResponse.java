package com.simbirsoft.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.simbirsoft.dto.product.ProductResponse;
import lombok.Getter;

@Getter
public class OrderItemResponse {
    private final int count;
    private final ProductResponse productResponse;

    public OrderItemResponse(
            @JsonProperty("count") int count,
            @JsonProperty("product") ProductResponse productResponse) {
        this.count = count;
        this.productResponse = productResponse;
    }
}
