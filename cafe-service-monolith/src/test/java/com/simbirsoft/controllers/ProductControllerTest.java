package com.simbirsoft.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simbirsoft.IntegrationTest;
import com.simbirsoft.dto.SortType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static com.simbirsoft.common.TestConstants.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@WithUserDetails(SIMPLE_USER_LOGIN)
@Sql(value = {"/sql/generate-users.sql", "/sql/generate-products.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/products-after.sql", "/sql/users-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("[200] GET /api/products?page=?&pageSize=?&producer=?&category=?&query=?&sortType=? get products by filter in successful case")
    void getProductsByFilter() throws Exception {
        int page = 0;
        int pageSize = 3;
        String producer = "producer2";
        String category = "category2";
        String query = "pro";
        SortType sortType = SortType.PRICE;

        int expectedTotalPages = 2;
        boolean expectedHasNext = true;

        mockMvc.perform(get("/api/products")
                        .param("page", String.valueOf(page))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("producer", producer)
                        .param("category", category)
                        .param("query", query)
                        .param("sortType", sortType.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasSize(pageSize)))
                .andExpect(jsonPath("$.total_pages", is(expectedTotalPages)))
                .andExpect(jsonPath("$.has_next", is(expectedHasNext)));

    }

    @Test
    @DisplayName("[200] GET /api/products/{productId} get product by id in successful case")
    void getProductById() throws Exception {
        mockMvc.perform(get("/api/products/" + PRODUCT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(PRODUCT_ID)))
                .andExpect(jsonPath("$.title", is(PRODUCT_TITLE)))
                .andExpect(jsonPath("$.description", is(PRODUCT_DESCRIPTION)))
                .andExpect(jsonPath("$.category", is(PRODUCT_CATEGORY)))
                .andExpect(jsonPath("$.producer", is(PRODUCT_PRODUCER)))
                .andExpect(jsonPath("$.availability", is(PRODUCT_AVAILABILITY)))
                .andExpect(jsonPath("$.image_path", is(PRODUCT_IMAGE_PATH)));
    }

    @Test
    @DisplayName("[200] GET /api/products/parameters get products parameters in successful case")
    void getProductsParameters() throws Exception {
        int expectedProducersSize = 2;
        int expectedCategoriesSize = 2;
        int expectedFilterTypes = 2;

        mockMvc.perform(get("/api/products/parameters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.producers", hasSize(expectedProducersSize)))
                .andExpect(jsonPath("$.categories", hasSize(expectedCategoriesSize)))
                .andExpect(jsonPath("$.filters", hasSize(expectedFilterTypes)));
    }
}