package com.simbirsoft.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class CreateOrderRequest {
    private final String comment;
    private final List<CreateOrderItemRequest> orderItemRequestList;

    public CreateOrderRequest(
            @JsonProperty("comment") String comment,
            @JsonProperty("items") List<CreateOrderItemRequest> orderItemRequestList) {
        this.comment = comment;
        this.orderItemRequestList = orderItemRequestList;
    }
}
