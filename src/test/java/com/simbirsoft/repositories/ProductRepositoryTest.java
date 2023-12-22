package com.simbirsoft.repositories;

import com.simbirsoft.DBTest;
import com.simbirsoft.domain.Product;
import com.simbirsoft.repositories.projection.ProductParametersProjection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DBTest
@Sql(scripts = {"/sql/generate-products.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/sql/products-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("find products by params with | category |")
    void findProductsByParamsWithCategory() {
        String category = "category1";
        Page<Product> productPage = productRepository.findProductsByParams(
                category, null, "", null
        );
        long actualProductsCount = productPage.get().count();
        long expectProductCount = 7L;
        assertEquals(expectProductCount, actualProductsCount);
    }

    @Test
    @DisplayName("find products by params with | producer |")
    void findProductsByParamsWithProducer() {
        String producer = "producer1";
        Page<Product> productPage = productRepository.findProductsByParams(
                null, producer, "", null
        );
        long actualProductsCount = productPage.get().count();
        long expectProductCount = 7L;
        assertEquals(expectProductCount, actualProductsCount);
    }

    @Test
    @DisplayName("find products by params with | query |")
    void findProductsByParamsWithQuery() {
        String query = "tova";
        Page<Product> productPage = productRepository.findProductsByParams(
                null, null, query, null
        );
        long actualProductsCount = productPage.get().count();
        long expectProductCount = 3L;
        assertEquals(expectProductCount, actualProductsCount);
        productPage.get().forEach(product -> {
            assertTrue(product.getTitle().toUpperCase().contains(query.toUpperCase()));
        });
    }

    @Test
    @DisplayName("find products by params with | producer & category | ")
    void findProductsByParamsWithProducerAndCategory() {
        String producer = "producer1";
        String category = "category1";
        Page<Product> productPage = productRepository.findProductsByParams(
                category, producer, "", null
        );
        long actualProductsCount = productPage.get().count();
        long expectProductCount = 3L;
        assertEquals(expectProductCount, actualProductsCount);
    }

    @Test
    @DisplayName("find products by params with | producer & category & sorted by price | ")
    void findProductsByParamsWithProducerAndCategoryAndSortedByPrice() {
        String producer = "producer1";
        String category = "category1";
        Sort sort = Sort.by(Sort.Direction.ASC, "price");
        Pageable pageable = PageRequest.of(0, 100, sort);

        Page<Product> productPage = productRepository.findProductsByParams(
                category, producer, "", pageable
        );
        long actualProductsCount = productPage.get().count();
        long expectProductCount = 3L;
        assertEquals(expectProductCount, actualProductsCount);

        float previousPrice = Float.MIN_VALUE;
        for (Product product: productPage.getContent()) {
            assertTrue(product.getPrice() > previousPrice);
            previousPrice = product.getPrice();
        }
    }

    @Test
    @DisplayName("")
    void getAllProducersAndCategories() {
        List<ProductParametersProjection> parameters = productRepository.getAllProducersAndCategories();
        int producersCountActual = 0;
        int categoriesCountActual = 0;
        int producersCountExpected = 2;
        int categoriesCountExpected = 2;
        for (ProductParametersProjection parameter : parameters) {
            if (parameter.getParameterName().equals("producer")) {
                producersCountActual++;
            } else if (parameter.getParameterName().equals("category")){
                categoriesCountActual++;
            }
        }
        assertEquals(producersCountExpected, producersCountActual);
        assertEquals(categoriesCountExpected, categoriesCountActual);
    }
}