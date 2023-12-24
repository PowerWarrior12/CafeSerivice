package com.simbirsoft.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simbirsoft.IntegrationTest;
import com.simbirsoft.common.BasketGenerator;
import com.simbirsoft.dto.basket.AddProductIntoBasketRequest;
import com.simbirsoft.dto.basket.UpdateBasketRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static com.simbirsoft.common.TestConstants.SIMPLE_USER_LOGIN;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.simbirsoft.constants.OkMessages.*;

@IntegrationTest
@WithUserDetails(SIMPLE_USER_LOGIN)
@Sql(value = {"/sql/generate-users.sql", "/sql/generate-products.sql", "/sql/generate-basket-items.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/basket-items-after.sql", "/sql/products-after.sql", "/sql/users-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BasketControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("[200] POST /api/basket add product to basket process in successful case")
    void addProductToBasket() throws Exception {
        AddProductIntoBasketRequest request = BasketGenerator.generateAddProductIntoBasketRequest();
        int expectedIdOfBasketItem = 17;

        mockMvc.perform(post("/api/basket")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(expectedIdOfBasketItem)));
    }

    @Test
    @DisplayName("[200] PUT /api/basket update basket item process in successful case")
    void updateBasket() throws Exception {
        UpdateBasketRequest request = BasketGenerator.generateUpdateBasketRequest();

        mockMvc.perform(put("/api/basket")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(BASKET_UPDATED_OK)));
    }

    @Test
    @DisplayName("[200] GET /api/basket get basket items process in successful case")
    void getBasket() throws Exception {
        int expectedBasketItemsSize = 3;
        int expectedBasketItemCount = 1;

        mockMvc.perform(get("/api/basket"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(expectedBasketItemsSize)))
                .andExpect(jsonPath("$.items[0].product", notNullValue()))
                .andExpect(jsonPath("$.items[0].count", is(expectedBasketItemCount)));
    }
}