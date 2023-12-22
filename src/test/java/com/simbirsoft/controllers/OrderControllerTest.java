package com.simbirsoft.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simbirsoft.IntegrationTest;
import com.simbirsoft.common.OrderGenerator;
import com.simbirsoft.dto.order.CreateOrderRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static com.simbirsoft.common.TestConstants.SIMPLE_USER_LOGIN;
import static com.simbirsoft.constants.OkMessages.ADD_ORDER_OK;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@WithUserDetails(SIMPLE_USER_LOGIN)
@Sql(value = {"/sql/generate-users.sql", "/sql/generate-products.sql", "/sql/generate-orders.sql", "/sql/generate-order-items.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/orders-items-after.sql", "/sql/orders-after.sql", "/sql/products-after.sql", "/sql/users-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("[200] GET /api/orders?page=?&pageSize=? get orders by user in successful case")
    void getOrders() throws Exception {
        int pageSize = 3;
        int page = 0;
        int expectedTotalPageCount = 1;
        boolean expectedHasNext = false;
        int expectedOrdersCount = 3;

        mockMvc.perform(get("/api/orders")
                        .param("page", String.valueOf(page))
                        .param("pageSize", String.valueOf(pageSize)))
                .andExpect(jsonPath("$.total_pages", is(expectedTotalPageCount)))
                .andExpect(jsonPath("$.has_next", is(expectedHasNext)))
                .andExpect(jsonPath("$.orders", hasSize(expectedOrdersCount)))
                .andExpect(jsonPath("$.orders[*].items").isNotEmpty())
                .andExpect(jsonPath("$.orders[*].items[*].product", notNullValue()));
    }

    @Test
    @DisplayName("[200] POST /api/orders create order process in successful case")
    void createOrder() throws Exception {
        CreateOrderRequest request = OrderGenerator.generateCreateOrderRequest();

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(ADD_ORDER_OK)));
    }
}