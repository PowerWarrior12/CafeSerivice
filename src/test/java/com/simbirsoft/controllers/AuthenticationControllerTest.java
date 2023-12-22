package com.simbirsoft.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simbirsoft.IntegrationTest;
import com.simbirsoft.common.KeyGenerator;
import com.simbirsoft.common.UserGenerator;
import com.simbirsoft.domain.User;
import com.simbirsoft.dto.authentication.AuthenticationRequest;
import com.simbirsoft.dto.user.RegistrationRequest;
import com.simbirsoft.dto.user.ResetPasswordRequest;
import com.simbirsoft.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestTemplate;

import static com.simbirsoft.common.TestConstants.*;
import static com.simbirsoft.constants.ErrorMessages.*;
import static com.simbirsoft.constants.OkMessages.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@IntegrationTest
@Sql(value = {"/sql/generate-users.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/users-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("[200] POST /api/auth/register - registration process in successful case")
    void register() throws Exception {
        RegistrationRequest request = UserGenerator.generateRegistrationRequest();

        userRepository.deleteById(USER_ID);

        mockMvc.perform(post("/api/auth/register")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(REGISTRATION_OK)));
    }

    @Test
    @DisplayName("[200] POST /api/auth/login - login process in successful case")
    void login() throws Exception {
        AuthenticationRequest request = UserGenerator.generateAuthenticationRequest();

        ResultActions resultActions = mockMvc.perform(post("/api/auth/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is(any(String.class))))
                .andExpect(jsonPath("$.user.login", is(USER_LOGIN)))
                .andExpect(jsonPath("$.user.first_name", is(USER_FIRST_NAME)))
                .andExpect(jsonPath("$.user.last_name", is(USER_LAST_NAME)))
                .andExpect(jsonPath("$.user.address", is(USER_ADDRESS)))
                .andExpect(jsonPath("$.user.phone_number", is(USER_PHONE_NUMBER)));
    }

    @Test
    @DisplayName("[200] GET /api/auth/forgot/{login} - forgot password process in successful case")
    void forgotPassword() throws Exception {
        mockMvc.perform(get("/api/auth/forgot/" + USER_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(SEND_RESET_PASSWORD_CODE_OK)));
    }

    @Test
    @DisplayName("[200] POST /api/auth/reset/{resetKey} - reset password process in successful case")
    void resetPassword() throws Exception {
        restTemplate.getForObject("/api/auth/forgot/" + USER_LOGIN, String.class);
        User user = userRepository.findByLogin(USER_LOGIN).orElseThrow(() -> new Exception(LOGIN_NOT_FOUND));
        ResetPasswordRequest request = UserGenerator.generateResetPasswordRequest();

        mockMvc.perform(post("/api/auth/reset/" + user.getResetPasswordKey())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(RESET_PASSWORD_OK)));
    }

    @Test
    @DisplayName("[200] PATCH /api/auth/activate/{activationKey} - activation process in successful case")
    void activateAccount() throws Exception {
        mockMvc.perform(patch("/api/auth/activate/431251")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(ACTIVATION_OK)));
    }
}