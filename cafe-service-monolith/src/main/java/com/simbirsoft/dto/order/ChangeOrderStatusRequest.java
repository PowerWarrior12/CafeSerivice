package com.simbirsoft.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.simbirsoft.domain.enums.OrderStatus;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ChangeOrderStatusRequest {
    private final OrderStatus status;
    private final UUID orderCode;

    public ChangeOrderStatusRequest(
            @JsonProperty("status") OrderStatus status,
            @JsonProperty("order_code") UUID orderCode) {
        this.status = status;
        this.orderCode = orderCode;
    }
}
