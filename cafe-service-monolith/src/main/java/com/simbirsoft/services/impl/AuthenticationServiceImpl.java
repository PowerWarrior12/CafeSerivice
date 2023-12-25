package com.simbirsoft.services.impl;

import com.simbirsoft.common.KeyGenerator;
import com.simbirsoft.domain.User;
import com.simbirsoft.dto.authentication.AuthenticationRequest;
import com.simbirsoft.dto.authentication.AuthenticationResponse;
import com.simbirsoft.dto.user.RegistrationRequest;
import com.simbirsoft.dto.user.RegistrationResponse;
import com.simbirsoft.dto.user.ResetPasswordRequest;
import com.simbirsoft.exceptions.ApiRequestException;
import com.simbirsoft.mail.MessageSender;
import com.simbirsoft.repositories.UserRepository;
import com.simbirsoft.security.JwtProvider;
import com.simbirsoft.services.AuthenticationService;
import com.simbirsoft.services.mappers.authentication.AuthenticationMapper;
import com.simbirsoft.services.mappers.user.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.simbirsoft.constants.ErrorMessages.*;
import static com.simbirsoft.constants.OkMessages.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationMapper authenticationMapper;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final MessageSender messageSender;
    private final KeyGenerator keyGenerator;
    @Value("${hostname}")
    private String hostname;
    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        try {
            log.atInfo().log(String.format("Start authorize process user with login: %s", request.getLogin()));

            User user = userRepository.findByLogin(request.getLogin())
                    .orElseThrow(() -> {
                        log.atInfo().log(String.format("User has incorrect login %s", request.getLogin()));
                        return new ApiRequestException(INCORRECT_LOGIN, HttpStatus.NOT_FOUND);
                    });
            Authentication authentication = authenticationManager.authenticate(
                    authenticationMapper.authenticationRequestToAuthentication(request)
            );
            String token = jwtProvider.generateToken(authentication);

            return authenticationMapper.getAuthenticationResponse(token, user);
        } catch (AuthenticationException exception) {
            log.atInfo().log(String.format("User with login %s has incorrect password", request.getLogin()));
            throw new ApiRequestException(INCORRECT_PASSWORD, HttpStatus.FORBIDDEN);
        }
    }

    @Override
    @Transactional
    public RegistrationResponse register(RegistrationRequest request) {
        log.atInfo().log("Registration process: start for user");
        if (userRepository.findByLogin(request.getLogin()).isPresent()) {
            log.atInfo().log(String.format("Registration process: failed with already exists login: %s", request.getLogin()));
            throw new ApiRequestException(ALREADY_EXISTS_LOGIN, HttpStatus.BAD_REQUEST);
        }
        if (!request.getPassword().equals(request.getPassword2())) {
            log.atInfo().log("Registration process: user put don't match passwords");
            throw new ApiRequestException(PASSWORDS_DO_NOT_MATCH, HttpStatus.BAD_REQUEST);
        }

        String activationKey = keyGenerator.generateKey();

        User user = userMapper.registrationRequestToUser(request, activationKey);
        userRepository.saveAndFlush(user);

        sendEmail(user, "Activation key", "activation-key-template", "activationUrl", "/api/auth/activate/" + user.getActivationKey());

        return new RegistrationResponse(REGISTRATION_OK);
    }

    @Override
    @Transactional
    public String activate(String key) {
        log.atInfo().log(String.format("Start activation process with key: %s", key));
        User user = userRepository.findByActivationKey(key)
                .orElseThrow(() -> {
                    log.atInfo().log(String.format("Can't activate account with activation key: %s", key));
                    return new ApiRequestException(INCORRECT_ACTIVATION_PROCESS, HttpStatus.NOT_FOUND);
                });
        user.setActivated(true);
        user.setActivationKey(null);
        userRepository.saveAndFlush(user);
        return ACTIVATION_OK;
    }

    @Override
    @Transactional
    public String sendResetPasswordCode(String login) {
        log.atInfo().log(String.format("Start send reset password code process for login %s", login));
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> {
                    log.atInfo().log(String.format("Send reset password code for login %s failed. Login not exists.", login));
                    return new ApiRequestException(LOGIN_NOT_FOUND, HttpStatus.NOT_FOUND);
                });
        String resetKey = keyGenerator.generateKey();
        user.setResetPasswordKey(resetKey);
        userRepository.saveAndFlush(user);
        sendEmail(user, "Password reset", "password-reset-template", "resetUrl","/api/auth/reset/" + resetKey);
        return SEND_RESET_PASSWORD_CODE_OK;
    }

    @Override
    @Transactional
    public String resetPassword(String resetPasswordCode, ResetPasswordRequest resetPasswordRequest) {
        log.atInfo().log(String.format("Reset password process: start for user with reset password key: %s", resetPasswordCode));
        User user = userRepository.findByResetPasswordKey(resetPasswordCode)
                .orElseThrow(() -> {
                    log.atInfo().log(String.format("Reset password process: failed for user with reset password key: %s", resetPasswordCode));
                    return new ApiRequestException(INCORRECT_RESET_PASSWORD_PROCESS, HttpStatus.NOT_FOUND);
                });
        if (!resetPasswordRequest.getPassword().equals(resetPasswordRequest.getPassword2())) {
            log.atInfo().log(String.format("Reset password process: user with login %s put don't match passwords", user.getLogin()));
            throw new ApiRequestException(PASSWORDS_DO_NOT_MATCH, HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
        userRepository.saveAndFlush(user);

        return RESET_PASSWORD_OK;
    }

    private void sendEmail(User user, String subject, String template, String urlAttribute, String urlPath) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("firstName", user.getFirstName());
        attributes.put(urlAttribute, "http://" + hostname + urlPath);
        messageSender.sendMessage(user.getLogin(), subject, template, attributes);
    }
}
