package com.simbirsoft.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.simbirsoft.dto.SortType;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ProductFilter {
    private final String category;
    private final String producer;
    private final SortType sort;
    private final String query;
    private final int page;
    private final int pageSize;

    public ProductFilter(
            @JsonProperty("category") @Nullable String category,
            @JsonProperty("producer") @Nullable String producer,
            @JsonProperty("sort") @Nullable SortType sort,
            @JsonProperty("query") @Nullable String query,
            @JsonProperty("page") int page,
            @JsonProperty("page_size") int pageSize) {
        this.category = category;
        this.producer = producer;
        this.sort = sort;
        this.query = query;
        this.pageSize = pageSize;
        this.page = page;
    }
}
