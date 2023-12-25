package com.simbirsoft.repositories;

import com.simbirsoft.DBTest;
import com.simbirsoft.domain.Order;
import com.simbirsoft.repositories.projection.DailyOrdersReportProjection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

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

    @Test
    @DisplayName("get daily orders report")
    void getDailyOrdersReportProjection() {
        List<DailyOrdersReportProjection> resultList = orderRepository.getDailyOrdersReport();
        resultList.forEach((result) -> {
            System.out.printf("%s -- %s -- %s -- %s%n", result.getDate(), result.getProductTitle(), result.getCount(), result.getTotalPrice());
        });
        assertEquals(resultList.size(), 3);

        assertEquals(resultList.get(0).getProductTitle(), "product1");
        assertEquals(resultList.get(1).getProductTitle(), "product5");
        assertEquals(resultList.get(2).getProductTitle(), "product3");

        assertEquals(resultList.get(0).getCount(), 1);
        assertEquals(resultList.get(1).getCount(), 2);
        assertEquals(resultList.get(2).getCount(), 5);

        assertEquals(resultList.get(0).getTotalPrice(), 100.0);
        assertEquals(resultList.get(1).getTotalPrice(), 120.0);
        assertEquals(resultList.get(2).getTotalPrice(), 650.0);
    }
}