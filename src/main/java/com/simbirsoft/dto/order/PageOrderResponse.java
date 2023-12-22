package com.simbirsoft.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class PageOrderResponse {
    @JsonProperty("total_pages")
    private final int totalPages;
    @JsonProperty("has_next")
    private final boolean hasNext;
    @JsonProperty("orders")
    private final List<CustomerOrderResponse> orders;
}
