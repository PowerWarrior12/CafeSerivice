package com.simbirsoft.services.impl;

import com.simbirsoft.domain.Order;
import com.simbirsoft.dto.order.*;
import com.simbirsoft.exceptions.ApiRequestException;
import com.simbirsoft.repositories.OrderRepository;
import com.simbirsoft.services.NotificationService;
import com.simbirsoft.services.OrderService;
import com.simbirsoft.services.mappers.order.OrderMapper;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.simbirsoft.constants.ErrorMessages.ORDER_NOT_FOUNT;

@Service
@Primary
public class OrderServiceNotified implements OrderService {
    private final OrderService orderService;
    private EntityManager entityManager;
    private final NotificationService notificationService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderServiceNotified (
            @Qualifier(value = "orderService") OrderService orderService,
            EntityManager entityManager,
            NotificationService notificationService,
            OrderRepository orderRepository,
            OrderMapper orderMapper) {
        this.orderService = orderService;
        this.entityManager = entityManager;
        this.notificationService = notificationService;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public PageOrderResponse getCustomersOrders(String customerLogin, CustomerOrderFilterRequest request) {
        return orderService.getCustomersOrders(customerLogin, request);
    }

    @Override
    public List<CustomerOrderResponse> getAllOrdersByLogins(OrderFilterRequest filterRequest) {
        return orderService.getAllOrdersByLogins(filterRequest);
    }

    @Override
    public UUID addOrder(CreateOrderRequest createOrderRequest, String login) {
        UUID orderCode = orderService.addOrder(createOrderRequest, login);
        entityManager.clear();
        Order createdOrder = orderRepository.findOrderByCode(orderCode).orElseThrow(() ->
                new ApiRequestException(ORDER_NOT_FOUNT, HttpStatus.NOT_FOUND)
        );
        notificationService.notifyAboutOrderCreation(orderMapper.orderToCustomerOrderResponse(createdOrder));
        return orderCode;
    }

    @Override
    public CustomerOrderResponse changeOrderStatus(ChangeOrderStatusRequest request) {
        CustomerOrderResponse order = orderService.changeOrderStatus(request);
        notificationService.notifyAboutOrderStatusChanged(order);
        return order;
    }
}
