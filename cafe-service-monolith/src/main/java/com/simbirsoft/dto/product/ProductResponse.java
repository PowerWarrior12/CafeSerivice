package com.simbirsoft.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductResponse {
    @JsonProperty("id")
    private final int id;
    @JsonProperty("title")
    private final String title;
    @JsonProperty("price")
    private final float price;
    @JsonProperty("description")
    private final String description;
    @JsonProperty("category")
    private final String category;
    @JsonProperty("producer")
    private final String producer;
    @JsonProperty("availability")
    private final boolean availability;
    @JsonProperty("image_path")
    private final String imagePath;
}
