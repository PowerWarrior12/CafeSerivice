package com.simbirsoft.common;

import com.simbirsoft.domain.User;
import com.simbirsoft.dto.authentication.AuthenticationRequest;
import com.simbirsoft.dto.user.RegistrationRequest;
import com.simbirsoft.dto.user.ResetPasswordRequest;

import static com.simbirsoft.common.TestConstants.*;

public class UserGenerator {
    public static User generateUser() {
        User user = new User();
        user.setId(USER_ID);
        user.setLogin(USER_LOGIN);
        user.setPassword(USER_PASSWORD_ENCODED);
        user.setFirstName(USER_FIRST_NAME);
        user.setLastName(USER_LAST_NAME);
        user.setAddress(USER_ADDRESS);
        user.setPhoneNumber(USER_PHONE_NUMBER);
        return user;
    }

    public static User generateUserWithNewPassword() {
        User user = generateUser();
        user.setPassword(USER_NEW_PASSWORD_ENCODED);
        return user;
    }

    public static RegistrationRequest generateRegistrationRequest() {
        return new RegistrationRequest(
                USER_LOGIN,
                USER_FIRST_NAME,
                USER_LAST_NAME,
                USER_PASSWORD_DECODED,
                USER_PASSWORD_DECODED,
                USER_PHONE_NUMBER,
                USER_ADDRESS
        );
    }

    public static AuthenticationRequest generateAuthenticationRequest() {
        return new AuthenticationRequest(
                USER_LOGIN,
                USER_PASSWORD_DECODED
        );
    }

    public static ResetPasswordRequest generateResetPasswordRequest() {
        return new ResetPasswordRequest(
                USER_NEW_PASSWORD_DECODED,
                USER_NEW_PASSWORD_DECODED
        );
    }
}
