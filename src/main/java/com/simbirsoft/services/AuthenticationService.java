package com.simbirsoft.services;

import com.simbirsoft.dto.authentication.AuthenticationRequest;
import com.simbirsoft.dto.authentication.AuthenticationResponse;
import com.simbirsoft.dto.user.RegistrationRequest;
import com.simbirsoft.dto.user.RegistrationResponse;
import com.simbirsoft.dto.user.ResetPasswordRequest;

public interface AuthenticationService {
    AuthenticationResponse login(AuthenticationRequest request);
    RegistrationResponse register(RegistrationRequest request);
    String activate(String key);
    String sendResetPasswordCode(String login);
    String resetPassword(String resetPasswordCode, ResetPasswordRequest resetPasswordRequest);
}
