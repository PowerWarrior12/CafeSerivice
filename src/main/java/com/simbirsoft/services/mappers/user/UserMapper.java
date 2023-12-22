package com.simbirsoft.services.mappers.user;

import com.simbirsoft.domain.User;
import com.simbirsoft.dto.user.RegistrationRequest;
import com.simbirsoft.dto.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;
    public User registrationRequestToUser(RegistrationRequest request, String activationKey) {
        User user = new User();
        user.setLogin(request.getLogin());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setActivated(false);
        user.setActivationKey(activationKey);
        return user;
    }

    public UserResponse userToUserResponse(User user) {
        return new UserResponse(
                user.getLogin(),
                user.getFirstName(),
                user.getLastName(),
                user.getAddress(),
                user.getPhoneNumber()
        );
    }
}
