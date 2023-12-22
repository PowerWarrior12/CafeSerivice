package com.simbirsoft.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.simbirsoft.dto.SortType;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductParametersResponse {
    private final List<String> producers;
    private final List<String> categories;
    private final List<SortType> filters;

    public ProductParametersResponse(
            @JsonProperty("producers") List<String> producers,
            @JsonProperty("categories") List<String> categories,
            @JsonProperty("filters") List<SortType> filters) {
        this.producers = producers;
        this.categories = categories;
        this.filters = filters;
    }
}
