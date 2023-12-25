package com.simbirsoft.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CreateOrderItemRequest {

    private final int count;
    private final int productId;

    public CreateOrderItemRequest(
            @JsonProperty("count") @Min(1) int count,
            @JsonProperty("product_id") int productId) {
        this.count = count;
        this.productId = productId;
    }
}
