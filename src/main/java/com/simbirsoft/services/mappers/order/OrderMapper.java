package com.simbirsoft.services.mappers.order;

import com.simbirsoft.domain.Order;
import com.simbirsoft.domain.enums.OrderStatus;
import com.simbirsoft.dto.order.CreateOrderRequest;
import com.simbirsoft.dto.order.CustomerOrderResponse;
import com.simbirsoft.dto.order.PageOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final OrderItemMapper orderItemMapper;
    public CustomerOrderResponse orderToCustomerOrderResponse(Order order) {
        return new CustomerOrderResponse(
                order.getCode(),
                order.getDate(),
                order.getStatus(),
                order.getComment(),
                order.getOrderItemSet()
                        .stream()
                        .map(orderItemMapper::orderItemToOrderItemResponse)
                        .toList()
        );
    }

    public Order createOrderRequestToOrder(CreateOrderRequest request, int customerId) {
        Order order = new Order();
        UUID orderCode = UUID.randomUUID();
        order.setCode(orderCode);
        order.setDate(LocalDateTime.now());
        order.setStatus(OrderStatus.RECEIVED);
        order.setCustomerId(customerId);
        return order;
    }
    public PageOrderResponse pageOrderToPageOrderResponse(Page<Order> page) {
        return new PageOrderResponse(
                page.getTotalPages(),
                page.hasNext(),
                page.stream()
                        .map(this::orderToCustomerOrderResponse)
                        .toList()
        );
    }
}
