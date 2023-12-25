package com.simbirsoft.common;

import com.simbirsoft.dto.order.CreateOrderItemRequest;
import com.simbirsoft.dto.order.CreateOrderRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.simbirsoft.common.TestConstants.ORDER_COMMENT;
import static com.simbirsoft.common.TestConstants.ORDER_ITEMS_COUNT;

public class OrderGenerator {
    private static final Random random = new Random();
    public static CreateOrderRequest generateCreateOrderRequest() {
        List<CreateOrderItemRequest> items = new ArrayList<>();
        for (int i = 0; i < ORDER_ITEMS_COUNT; i++) {
            items.add(generateCreateOrderItemRequest());
        }
        return new CreateOrderRequest(
                ORDER_COMMENT,
                items
        );
    }
    public static CreateOrderItemRequest generateCreateOrderItemRequest() {
        return new CreateOrderItemRequest(
                random.nextInt(10) + 1,
                random.nextInt(10) + 1
        );
    }
}
