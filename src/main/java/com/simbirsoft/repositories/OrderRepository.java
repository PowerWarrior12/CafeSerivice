package com.simbirsoft.repositories;

import com.simbirsoft.domain.Order;
import com.simbirsoft.repositories.projection.DailyOrdersReportProjection;
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

    @Query("select function('DATE_TRUNC', 'day', order.date) as date," +
            " product.title as productTitle," +
            " sum(orderItem.count) as count," +
            " sum(product.price * orderItem.count) as totalPrice" +
            " from Order order" +
            " join fetch OrderItem orderItem on order.code = orderItem.orderCode" +
            " join fetch Product product on orderItem.productId = product.id" +
            " where order.status = 'READY'" +
            " group by function('DATE_TRUNC', 'day', order.date), product.id" +
            " order by function('DATE_TRUNC', 'day', order.date) asc")
    List<DailyOrdersReportProjection> getDailyOrdersReport();

    static int DEFAULT_PAGE_SIZE = 100;
}
