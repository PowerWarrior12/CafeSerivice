package com.simbirsoft.services.mappers.basket;

import com.simbirsoft.domain.BasketItem;
import com.simbirsoft.dto.basket.AddProductIntoBasketRequest;
import com.simbirsoft.dto.basket.BasketItemResponse;
import com.simbirsoft.dto.basket.BasketResponse;
import com.simbirsoft.services.mappers.product.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BasketMapper {
    private final ProductMapper productMapper;
    public BasketItem addProductIntoBasketRequestToBasketItem(AddProductIntoBasketRequest request, int userId) {
        BasketItem basketItem = new BasketItem();
        basketItem.setCount(request.getCount());
        basketItem.setProductId(request.getProductId());
        basketItem.setUserId(userId);
        return basketItem;
    }
    public BasketItemResponse basketItemToBasketItemResponse(BasketItem basketItem) {
        return new BasketItemResponse(
                basketItem.getId(),
                productMapper.productToProductResponse(basketItem.getProduct()),
                basketItem.getCount()
        );
    }
    public BasketResponse basketItemsToBasketResponse(List<BasketItem> items) {
        return new BasketResponse(
                items.stream()
                        .map(this::basketItemToBasketItemResponse)
                        .toList()
        );
    }
}
