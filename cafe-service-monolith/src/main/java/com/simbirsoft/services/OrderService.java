package com.simbirsoft.services;

import com.simbirsoft.dto.order.*;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    PageOrderResponse getCustomersOrders(String customerLogin, CustomerOrderFilterRequest request);
    List<CustomerOrderResponse> getAllOrdersByLogins(OrderFilterRequest filterRequest);
    UUID addOrder(CreateOrderRequest createOrderRequest, String login);
    CustomerOrderResponse changeOrderStatus(ChangeOrderStatusRequest request);
}
