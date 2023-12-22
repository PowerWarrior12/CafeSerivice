package com.simbirsoft.controllers;

import com.simbirsoft.domain.enums.OrderStatus;
import com.simbirsoft.dto.order.ChangeOrderStatusRequest;
import com.simbirsoft.dto.order.CustomerOrderResponse;
import com.simbirsoft.dto.order.OrderFilterRequest;
import com.simbirsoft.dto.product.CreateProductRequest;
import com.simbirsoft.dto.product.UpdateProductRequest;
import com.simbirsoft.services.OrderService;
import com.simbirsoft.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ProductService productService;
    private final OrderService orderService;

    @PostMapping("/products/add")
    public ResponseEntity<Integer> createProduct(
            @RequestPart(name = "product") @Valid CreateProductRequest request,
            @RequestPart(name = "image", required = false) MultipartFile image) {
        return ResponseEntity.ok(productService.addProduct(image, request));
    }

    @PostMapping("/products/update")
    public ResponseEntity<String> updateProduct(
            @RequestPart(name = "product") @Valid UpdateProductRequest request,
            @RequestPart(name = "image") MultipartFile image) {
        return ResponseEntity.ok(productService.updateProduct(image, request));
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") int productId) {
        return ResponseEntity.ok(productService.deleteProductById(productId));
    }

    @GetMapping("/orders/by-users-logins")
    public ResponseEntity<List<CustomerOrderResponse>> getAllOrdersByUsersIds(
            @RequestParam("usersLogins") List<String> usersLogins
    ) {
        return ResponseEntity.ok(orderService.getAllOrdersByLogins(new OrderFilterRequest(usersLogins)));
    }

    @PatchMapping("/orders/change-status")
    public ResponseEntity<String> changeOrderStatus(
            @RequestParam("status") OrderStatus status,
            @RequestParam("orderCode") UUID orderCode) {
        return ResponseEntity.ok(orderService.changeOrderStatus(new ChangeOrderStatusRequest(status, orderCode)));
    }
}
