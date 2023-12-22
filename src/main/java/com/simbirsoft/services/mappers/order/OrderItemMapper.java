package com.simbirsoft.services.mappers.order;

import com.simbirsoft.domain.OrderItem;
import com.simbirsoft.dto.order.CreateOrderItemRequest;
import com.simbirsoft.dto.order.OrderItemResponse;
import com.simbirsoft.services.mappers.product.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderItemMapper {
    private final ProductMapper productMapper;
    public OrderItemResponse orderItemToOrderItemResponse(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getCount(),
                productMapper.productToProductResponse(orderItem.getProduct())
        );
    }
    public OrderItem createOrderItemRequestToOrderItem(CreateOrderItemRequest request, UUID orderCode) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderCode(orderCode);
        orderItem.setCount(request.getCount());
        orderItem.setProductId(request.getProductId());
        return orderItem;
    }
}
