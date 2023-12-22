package com.simbirsoft.services.impl;

import com.simbirsoft.ServiceTest;
import com.simbirsoft.common.KeyGenerator;
import com.simbirsoft.common.UserGenerator;
import com.simbirsoft.domain.User;
import com.simbirsoft.domain.enums.Role;
import com.simbirsoft.dto.authentication.AuthenticationRequest;
import com.simbirsoft.dto.user.RegistrationRequest;
import com.simbirsoft.dto.user.RegistrationResponse;
import com.simbirsoft.dto.user.ResetPasswordRequest;
import com.simbirsoft.mail.MessageSender;
import com.simbirsoft.repositories.UserRepository;
import com.simbirsoft.security.JwtProvider;
import com.simbirsoft.services.mappers.authentication.AuthenticationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.simbirsoft.common.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ServiceTest
class AuthenticationServiceImplTest {
    @Autowired
    private AuthenticationServiceImpl authenticationService;
    @Autowired
    private AuthenticationMapper authenticationMapper;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private MessageSender messageSender;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private KeyGenerator keyGenerator;
    private User user;
    @Value("${hostname}")
    private String hostname;

    @BeforeEach
    void setUp() {
        user = UserGenerator.generateUser();
    }

    @Test
    @DisplayName("Login process by exists user")
    void login() {
        user = UserGenerator.generateUser();
        user.setActivated(true);
        user.setRoles(Collections.singleton(Role.ROLE_CUSTOMER));

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(USER_LOGIN, USER_PASSWORD_DECODED);

        when(userRepository.findByLogin(USER_LOGIN)).thenReturn(Optional.of(user));
        authenticationService.login(authenticationRequest);

        verify(userRepository, times(1)).findByLogin(USER_LOGIN);
        verify(jwtProvider, times(1)).generateToken(any());
        verify(authenticationManager, times(1)).
                authenticate(authenticationMapper.authenticationRequestToAuthentication(authenticationRequest));
    }

    @Test
    @DisplayName("Registration process in successful case")
    void register() {
        user.setActivationKey(USER_ACTIVATION_KEY);
        RegistrationRequest request = new RegistrationRequest(
                USER_LOGIN,
                USER_FIRST_NAME,
                USER_LAST_NAME,
                USER_PASSWORD_DECODED,
                USER_PASSWORD_DECODED,
                USER_PHONE_NUMBER,
                USER_ADDRESS
        );
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("firstName", USER_FIRST_NAME);
        attributes.put("activationUrl", "http://" + hostname + "/api/auth/activate/" + USER_ACTIVATION_KEY);

        when(userRepository.findByLogin(USER_LOGIN)).thenReturn(Optional.empty());
        when(userRepository.saveAndFlush(any())).thenReturn(user);
        when(keyGenerator.generateKey()).thenReturn(USER_ACTIVATION_KEY);

        RegistrationResponse registrationResponse = authenticationService.register(request);

        assertNotNull(registrationResponse.getMessage());
        InOrder inOrder = inOrder(userRepository);
        inOrder.verify(userRepository, times(1)).findByLogin(ArgumentMatchers.eq(USER_LOGIN));
        inOrder.verify(userRepository, times(1)).saveAndFlush(any());
        verify(messageSender, times(1))
                .sendMessage(
                        ArgumentMatchers.eq(USER_LOGIN),
                        ArgumentMatchers.eq("Activation key"),
                        ArgumentMatchers.eq("activation-key-template"),
                        ArgumentMatchers.eq(attributes));
    }

    @Test
    @DisplayName("Send reset password code process in successful case")
    void sendResetPasswordCode() {
        when(userRepository.findByLogin(USER_LOGIN)).thenReturn(Optional.of(user));
        when(keyGenerator.generateKey()).thenReturn(USER_RESET_PASSWORD_KEY);
        when(userRepository.saveAndFlush(any())).thenReturn(user);

        String result = authenticationService.sendResetPasswordCode(USER_LOGIN);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("firstName", USER_FIRST_NAME);
        attributes.put("resetUrl", "http://" + hostname + "/api/auth/reset/" + USER_RESET_PASSWORD_KEY);

        assertNotNull(result);
        InOrder inOrder = inOrder(userRepository);
        inOrder.verify(userRepository, times(1)).findByLogin(USER_LOGIN);
        inOrder.verify(userRepository, times(1)).saveAndFlush(any());
        verify(messageSender, times(1))
                .sendMessage(
                        ArgumentMatchers.eq(USER_LOGIN),
                        ArgumentMatchers.eq("Password reset"),
                        ArgumentMatchers.eq("password-reset-template"),
                        ArgumentMatchers.eq(attributes)
                );
    }

    @Test
    @DisplayName("Activate account process in successful case")
    void activate() {
        user.setActivationKey(USER_ACTIVATION_KEY);

        when(userRepository.findByActivationKey(USER_ACTIVATION_KEY)).thenReturn(Optional.of(user));
        when(userRepository.saveAndFlush(user)).thenReturn(user);

        String result = authenticationService.activate(USER_ACTIVATION_KEY);

        InOrder inOrder = inOrder(userRepository);
        assertNotNull(result);
        inOrder.verify(userRepository, times(1)).findByActivationKey(USER_ACTIVATION_KEY);
        inOrder.verify(userRepository, times(1)).saveAndFlush(user);
    }

    @Test
    @DisplayName("Reset password process in success case")
    void resetPassword() {
        user.setResetPasswordKey(USER_RESET_PASSWORD_KEY);
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest(
            USER_NEW_PASSWORD_DECODED, USER_NEW_PASSWORD_DECODED
        );
        User userWithNewPassword = UserGenerator.generateUserWithNewPassword();

        when(userRepository.findByResetPasswordKey(USER_RESET_PASSWORD_KEY)).thenReturn(Optional.of(user));
        when(userRepository.saveAndFlush(userWithNewPassword)).thenReturn(userWithNewPassword);

        String result = authenticationService.resetPassword(USER_RESET_PASSWORD_KEY, resetPasswordRequest);

        InOrder inOrder = inOrder(userRepository);
        assertNotNull(result);
        inOrder.verify(userRepository, times(1)).findByResetPasswordKey(USER_RESET_PASSWORD_KEY);
        inOrder.verify(userRepository, times(1)).saveAndFlush(userWithNewPassword);
    }
}