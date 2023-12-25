package com.simbirsoft.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.simbirsoft.domain.enums.OrderStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class CustomerOrderResponse {
    @JsonProperty("code")
    private final UUID code;
    @JsonProperty("date")
    private final LocalDateTime date;
    @JsonProperty("status")
    private final OrderStatus status;
    @JsonProperty("comment")
    private final String comment;
    @JsonProperty("items")
    private final List<OrderItemResponse> orderItemResponseList;
}
