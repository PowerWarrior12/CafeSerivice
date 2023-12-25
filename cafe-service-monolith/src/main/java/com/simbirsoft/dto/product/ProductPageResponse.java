package com.simbirsoft.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProductPageResponse {
    @JsonProperty("products")
    private final List<ProductResponse> products;
    @JsonProperty("total_pages")
    private final int totalPages;
    @JsonProperty("has_next")
    private final boolean hasNext;
}
