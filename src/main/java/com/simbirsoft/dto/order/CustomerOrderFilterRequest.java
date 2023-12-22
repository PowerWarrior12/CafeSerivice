package com.simbirsoft.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CustomerOrderFilterRequest {
    private final int page;
    private final int pageSize;

    public CustomerOrderFilterRequest(
            @JsonProperty("page") int page,
            @JsonProperty("page_size") int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }
}
