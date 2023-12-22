package com.simbirsoft.controllers;

import com.simbirsoft.dto.SortType;
import com.simbirsoft.dto.product.ProductFilter;
import com.simbirsoft.dto.product.ProductPageResponse;
import com.simbirsoft.dto.product.ProductParametersResponse;
import com.simbirsoft.dto.product.ProductResponse;
import com.simbirsoft.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @GetMapping
    public ResponseEntity<ProductPageResponse> getProductsByFilter(
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("producer") String producer,
            @RequestParam("category") String category,
            @RequestParam("query") String query,
            @RequestParam("sortType") SortType sortType
            ) {
        return ResponseEntity.ok(productService.getProductsByParams(
                new ProductFilter(category, producer, sortType, query, page, pageSize)));
    }
    @GetMapping("{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("productId") int productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }
    @GetMapping("/parameters")
    public ResponseEntity<ProductParametersResponse> getProductsParameters() {
        return ResponseEntity.ok(productService.getProductParameters());
    }
}
