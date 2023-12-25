package com.simbirsoft.repositories;

import com.simbirsoft.domain.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasketItemRepository extends JpaRepository<BasketItem, Integer> {
    @Query("select basketItem from BasketItem basketItem" +
            " left join fetch Product product on basketItem.productId = product.id" +
            " where :userId = basketItem.userId")
    List<BasketItem> findByUserId(
            @Param("userId") int userId
    );
}
