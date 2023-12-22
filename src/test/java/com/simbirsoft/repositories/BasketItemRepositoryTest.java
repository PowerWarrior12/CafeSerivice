package com.simbirsoft.repositories;

import com.simbirsoft.DBTest;
import com.simbirsoft.domain.BasketItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DBTest
@Sql(scripts = {
        "/sql/generate-users.sql",
        "/sql/generate-products.sql",
        "/sql/generate-basket-items.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/sql/basket-items-after.sql","/sql/products-after.sql","/sql/users-after.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BasketItemRepositoryTest {

    @Autowired
    private BasketItemRepository basketItemRepository;

    @Test
    @DisplayName("find basket items by user id")
    void findByUserId() {
        List<BasketItem> basketItemList = basketItemRepository.findByUserId(1);
        int expectedBasketItemsCount = 5;
        assertEquals(expectedBasketItemsCount, basketItemList.size());
        basketItemList.forEach(basketItem -> {
            assertNotNull(basketItem.getProduct().getImagePath());
        });
    }
}