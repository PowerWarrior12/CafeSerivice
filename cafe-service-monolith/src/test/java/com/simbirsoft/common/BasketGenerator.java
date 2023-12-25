package com.simbirsoft.common;

import com.simbirsoft.dto.basket.AddProductIntoBasketRequest;
import com.simbirsoft.dto.basket.UpdateBasketRequest;

import static com.simbirsoft.common.TestConstants.*;

public class BasketGenerator {
    public static AddProductIntoBasketRequest generateAddProductIntoBasketRequest() {
        return new AddProductIntoBasketRequest(
                BASKET_COUNT,
                BASKET_PRODUCT_ID
        );
    }

    public static UpdateBasketRequest generateUpdateBasketRequest() {
        return new UpdateBasketRequest(
                BASKET_ID,
                BASKET_COUNT
        );
    }
}
