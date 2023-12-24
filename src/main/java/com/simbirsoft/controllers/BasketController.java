package com.simbirsoft.controllers;

import com.simbirsoft.dto.basket.AddProductIntoBasketRequest;
import com.simbirsoft.dto.basket.BasketResponse;
import com.simbirsoft.dto.basket.UpdateBasketRequest;
import com.simbirsoft.services.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/basket")
@RequiredArgsConstructor
public class BasketController {
    private final BasketService basketService;
    @PostMapping
    public ResponseEntity<Integer> addProductToBasket(
            @RequestBody AddProductIntoBasketRequest request,
            Principal principal
    ) {
        return ResponseEntity.ok(basketService.addNewBasketItem(request, principal.getName()));
    }
    @PutMapping
    public ResponseEntity<String> updateBasket(
            @RequestBody UpdateBasketRequest request,
            Principal principal
    ) {
        return ResponseEntity.ok(basketService.updateBasket(principal.getName(), request));
    }
    @GetMapping
    public ResponseEntity<BasketResponse> getBasket(Principal principal) {
        return ResponseEntity.ok(basketService.getBasketByUsersLogin(principal.getName()));
    }
}
