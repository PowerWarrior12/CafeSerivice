package com.simbirsoft.repositories;

import com.simbirsoft.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query("select distinct order from Order order" +
            " left join OrderItem orderItem on orderItem.orderCode = order.code" +
            " left join fetch Product product on orderItem.productId = product.id" +
            " where :customerId = order.customerId")
    Page<Order> findOrderByCustomerId(
            @Param("customerId") int customerId,
            @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable);

    @Query("select distinct order from Order order" +
            " left join OrderItem orderItem on orderItem.orderCode = order.code" +
            " left join fetch Product product on orderItem.productId = product.id" +
            " where order.customerId in :customersIds")
    List<Order> findOrdersByCustomerIds(@Param("customersIds") List<Integer> customersIds);

    static int DEFAULT_PAGE_SIZE = 100;
}
