package com.simbirsoft.repositories;

import com.simbirsoft.domain.Product;
import com.simbirsoft.repositories.projection.ProductParametersProjection;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    /**
     * @param category May be null
     * @param producer May be null.
     * @param query Must not be null. If query is empty then need to use empty string.
     * @param pageable May be null.
     * @return Page of products based on received params.
     */
    @Query("select product from Product product" +
            " where (:categoryParam is null or product.category = :categoryParam)" +
            " and (:producerParam is null or product.producer = :producerParam)" +
            " and (:queryParam is null or upper(product.title) like upper(concat('%',:queryParam,'%')))" +
            " and (product.isDeleted = false)")
    Page<Product> findProductsByParams(
            @Param("categoryParam") @Nullable String category,
            @Param("producerParam") @Nullable String producer,
            @Param("queryParam") @Nullable String query,
            @PageableDefault(size = DEFAULT_PRODUCTS_PAGE) Pageable pageable
    );

    /**
     * Returns the value of the category or manufacturer in the first column,
     * returns the name of the parameter in the second column: "category" or "produce"
     */
    @Query("select distinct p.category as parameter, 'category' as parameterName from Product p" +
            " where p.isDeleted = false" +
            " union" +
            " select distinct p.producer as parameter, 'producer' as parameterName from Product p" +
            " where p.isDeleted = false")
    List<ProductParametersProjection> getAllProducersAndCategories();
    @Query("select p from Product p where p.isDeleted = false and p.id = :id")
    Optional<Product> findProductById(@Param("id") int id);

    // region constants
    public static final int DEFAULT_PRODUCTS_PAGE = 100;
    // endregion
}
