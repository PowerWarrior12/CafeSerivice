package com.simbirsoft.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simbirsoft.IntegrationTest;
import com.simbirsoft.common.ImageGenerator;
import com.simbirsoft.common.ProductGenerator;
import com.simbirsoft.domain.enums.OrderStatus;
import com.simbirsoft.dto.product.CreateProductRequest;
import com.simbirsoft.dto.product.UpdateProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static com.simbirsoft.common.TestConstants.*;
import static com.simbirsoft.constants.OkMessages.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@WithUserDetails(USER_LOGIN)
@Sql(value = {"/sql/generate-users.sql", "/sql/generate-products.sql", "/sql/generate-orders.sql", "/sql/generate-order-items.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/orders-items-after.sql", "/sql/orders-after.sql", "/sql/products-after.sql", "/sql/users-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    @Test
    @DisplayName("[200] POST /api/admin/products/add add product process in successful case")
    void createProduct() throws Exception {
        CreateProductRequest request = ProductGenerator.generateCreateProductRequest();
        MockMultipartFile imageMultipartFile = ImageGenerator.generateImageMockMultipartFile();
        MockMultipartFile requestMultipartFile = new MockMultipartFile(
                "product",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(request).getBytes());

        mockMvc.perform(multipart("/api/admin/products/add")
                        .file(imageMultipartFile)
                        .file(requestMultipartFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(17)));
    }

    @Test
    @DisplayName("[200] POST /api/admin/products/update update product process in successful case")
    void updateProduct() throws Exception {
        UpdateProductRequest request = ProductGenerator.generateUpdateProductRequest();
        MockMultipartFile imageMultipartFile = ImageGenerator.generateImageMockMultipartFile();
        MockMultipartFile requestMultipartFile = new MockMultipartFile(
                "product",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(request).getBytes()
        );

        mockMvc.perform(multipart("/api/admin/products/update")
                        .file(imageMultipartFile)
                        .file(requestMultipartFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(UPDATE_PRODUCT_OK)));
    }

    @Test
    @DisplayName("[200] DELETE /api/admin/products/{productId} delete product process in successful case")
    void deleteProduct() throws Exception {
        mockMvc.perform(delete("/api/admin/products/" + PRODUCT_ID)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(DELETE_PRODUCT_OK)));
    }

    @Test
    @DisplayName("[200] GET /api/admin/orders/by-users-logins?usersLogin=? get all orders by users process in successful case")
    void getAllOrdersByUsersIds() throws Exception {
        int expectedResultSize = 4;

        mockMvc.perform(get("/api/admin/orders/by-users-logins")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("usersLogins", USER_LOGIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].code").isNotEmpty())
                .andExpect(jsonPath("$.[*].date").isNotEmpty())
                .andExpect(jsonPath("$.[*].status").isNotEmpty())
                .andExpect(jsonPath("$.[*].comment").isNotEmpty())
                .andExpect(jsonPath("$.[*].items.length()").isNotEmpty())
                .andExpect(jsonPath("$", hasSize(expectedResultSize)));
    }

    @Test
    @DisplayName("[200] PATCH /api/admin/orders/change-status?status=?&orderCode=? change order status process in successful case")
    void changeOrderStatus() throws Exception {
        mockMvc.perform(patch("/api/admin/orders/change-status")
                        .param("status", OrderStatus.READY.name())
                        .param("orderCode", ORDER_CODE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(CHANGE_ORDER_STATUS_OK)));

    }
}