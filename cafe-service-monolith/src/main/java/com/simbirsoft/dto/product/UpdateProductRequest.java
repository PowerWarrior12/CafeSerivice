package com.simbirsoft.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UpdateProductRequest {
    private final int id;
    private final String title;
    private final float price;
    private final String description;
    private final String category;
    private final String producer;
    private final boolean availability;

    public UpdateProductRequest(
            @JsonProperty("id") int id,
            @JsonProperty("title") @NotBlank @NotNull String title,
            @JsonProperty("price") @PositiveOrZero float price,
            @JsonProperty("description") String description,
            @JsonProperty("category") @NotNull @NotBlank String category,
            @JsonProperty("producer") @NotNull @NotBlank String producer,
            @JsonProperty("availability") boolean availability) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.category = category;
        this.producer = producer;
        this.availability = availability;
    }
}
