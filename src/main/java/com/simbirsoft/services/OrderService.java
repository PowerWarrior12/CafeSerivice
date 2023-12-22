package com.simbirsoft.services;

import com.simbirsoft.dto.order.*;

import java.util.List;

public interface OrderService {
    PageOrderResponse getCustomersOrders(String customerLogin, CustomerOrderFilterRequest request);
    List<CustomerOrderResponse> getAllOrdersByLogins(OrderFilterRequest filterRequest);
    String addOrder(CreateOrderRequest createOrderRequest, String login);
    String changeOrderStatus(ChangeOrderStatusRequest request);
}
