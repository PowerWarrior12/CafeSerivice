package com.simbirsoft.repositories;

import com.simbirsoft.DBTest;
import com.simbirsoft.domain.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DBTest
@Sql(scripts = {
        "/sql/generate-users.sql",
        "/sql/generate-products.sql",
        "/sql/generate-orders.sql",
        "/sql/generate-order-items.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/sql/orders-items-after.sql", "/sql/orders-after.sql","/sql/products-after.sql","/sql/users-after.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("find order by customer id")
    void findOrderByCustomerId() {
        Page<Order> orderPage = orderRepository.findOrderByCustomerId(1, null);
        long expectedOrdersCount = 7L;
        long actualOrdersCount = orderPage.stream().count();
        assertEquals(expectedOrdersCount, actualOrdersCount);

        orderPage.get().forEach(order -> {
            assertEquals(1, order.getOrderItemSet().size());
            order.getOrderItemSet().forEach(orderItem -> {
                assertNotNull(orderItem.getProduct());
                assertNotNull(orderItem.getProduct().getTitle());
            });
        });
    }
}