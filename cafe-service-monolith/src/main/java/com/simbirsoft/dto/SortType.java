package com.simbirsoft.dto;

import lombok.Getter;

@Getter
public enum SortType {
    TITLE("title"), PRICE("price");
    private final String parameter;
    SortType(String parameter) {
        this.parameter = parameter;
    }
}
