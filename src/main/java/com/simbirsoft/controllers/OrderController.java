package com.simbirsoft.controllers;

import com.simbirsoft.dto.order.CreateOrderRequest;
import com.simbirsoft.dto.order.CustomerOrderFilterRequest;
import com.simbirsoft.dto.order.PageOrderResponse;
import com.simbirsoft.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<PageOrderResponse> getOrders(
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize,
            Principal principal
    ) {
        return ResponseEntity.ok(orderService.getCustomersOrders(principal.getName(),
                new CustomerOrderFilterRequest(page, pageSize)));
    }

    @PostMapping
    public ResponseEntity<String> createOrder(
            @RequestBody CreateOrderRequest createOrderRequest,
            Principal principal
    ) {
        return ResponseEntity.ok(orderService.addOrder(createOrderRequest, principal.getName()));
    }
}
