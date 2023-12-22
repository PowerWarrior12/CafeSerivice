package com.simbirsoft.dto.basket;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UpdateBasketRequest {
    private final int basketItemId;
    private final int count;

    public UpdateBasketRequest(
            @JsonProperty("basket_item_id") int basketItemId,
            @JsonProperty("count") @Min(0) int count) {
        this.basketItemId = basketItemId;
        this.count = count;
    }
}
