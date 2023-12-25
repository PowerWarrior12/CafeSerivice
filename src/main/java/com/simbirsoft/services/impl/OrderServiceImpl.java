package com.simbirsoft.services.impl;

import com.simbirsoft.domain.Order;
import com.simbirsoft.domain.OrderItem;
import com.simbirsoft.domain.User;
import com.simbirsoft.dto.order.*;
import com.simbirsoft.exceptions.ApiRequestException;
import com.simbirsoft.repositories.OrderItemRepository;
import com.simbirsoft.repositories.OrderRepository;
import com.simbirsoft.repositories.UserRepository;
import com.simbirsoft.services.OrderService;
import com.simbirsoft.services.mappers.order.OrderItemMapper;
import com.simbirsoft.services.mappers.order.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.simbirsoft.constants.ErrorMessages.LOGIN_NOT_FOUND;
import static com.simbirsoft.constants.ErrorMessages.ORDER_NOT_FOUNT;

@Service("orderService")
@RequiredArgsConstructor
@Log4j2
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional(readOnly = true)
    public PageOrderResponse getCustomersOrders(String customerLogin, CustomerOrderFilterRequest request) {
        log.atDebug().log(String.format("Get customers orders process: start for user with login %s", customerLogin));
        User user = userRepository.findByLogin(customerLogin)
                .orElseThrow(() -> {
                    log.atDebug().log(String.format("Get customers orders process: user with login %s are not exists",
                            customerLogin));
                    return new ApiRequestException(LOGIN_NOT_FOUND, HttpStatus.NOT_FOUND);
                });
        Page<Order> orderPage = orderRepository.findOrderByCustomerId(user.getId(), generatePageable(request));
        return orderMapper.pageOrderToPageOrderResponse(orderPage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerOrderResponse> getAllOrdersByLogins(OrderFilterRequest filterRequest) {
        log.atDebug().log(String.format("Get all orders by logins process: start for logins: %s", filterRequest.getCustomersLogins()));
        List<Integer> userIds = userRepository.findIdsByLogins(filterRequest.getCustomersLogins());
        if (userIds.size() != filterRequest.getCustomersLogins().size()) {
            log.atDebug().log("Get all orders by logins process: some logins are not exists");
        }
        List<Order> orderList = orderRepository.findOrdersByCustomerIds(userIds);
        return orderList.stream()
                .map(orderMapper::orderToCustomerOrderResponse)
                .toList();
    }

    @Override
    @Transactional
    public UUID addOrder(CreateOrderRequest createOrderRequest, String login) {
        log.atDebug().log(String.format("Add order process: start for login: %s and order: %s", login, createOrderRequest));
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> {
                    log.atDebug().log(String.format("Add order process: user with login %s are not exists", login));
                    return new ApiRequestException(LOGIN_NOT_FOUND, HttpStatus.NOT_FOUND);
                });
        Order order = orderMapper.createOrderRequestToOrder(createOrderRequest, user.getId());
        Set<OrderItem> orderItems = createOrderRequest.getOrderItemRequestList().stream()
                .map(request -> orderItemMapper.createOrderItemRequestToOrderItem(request, order.getCode()))
                .collect(Collectors.toSet());
        orderRepository.saveAndFlush(order);
        orderItemRepository.saveAllAndFlush(orderItems);

        return order.getCode();
    }

    @Override
    @Transactional
    public CustomerOrderResponse changeOrderStatus(ChangeOrderStatusRequest request) {
        log.atDebug().log(String.format("Change order status process: start with order code: %s and new order status: %s",
                request.getOrderCode(),
                request.getStatus()));
        Order order = orderRepository.findById(request.getOrderCode())
                .orElseThrow(() -> {
                    log.atDebug().log(String.format("Change order status process: order with code %s not exists", request.getOrderCode()));
                    return new ApiRequestException(ORDER_NOT_FOUNT, HttpStatus.NOT_FOUND);
                });
        order.setStatus(request.getStatus());
        orderRepository.flush();

        return orderMapper.orderToCustomerOrderResponse(order);
    }

    private Pageable generatePageable(CustomerOrderFilterRequest request) {
        return PageRequest.of(request.getPage(), request.getPageSize());
    }
}
