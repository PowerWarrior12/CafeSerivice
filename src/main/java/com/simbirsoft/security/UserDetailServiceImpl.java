package com.simbirsoft.security;

import com.simbirsoft.domain.User;
import com.simbirsoft.exceptions.ApiRequestException;
import com.simbirsoft.repositories.UserRepository;
import com.simbirsoft.services.mappers.authentication.AuthenticationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.simbirsoft.constants.ErrorMessages.ACCOUNT_DO_NOT_ACTIVATED;
import static com.simbirsoft.constants.ErrorMessages.LOGIN_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final AuthenticationMapper authenticationMapper;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username)
                .orElseThrow(() ->{
                    log.atInfo().log(String.format("Authorization process: not fount user with login %s", username));
                    return new UsernameNotFoundException(LOGIN_NOT_FOUND);
                });
        if (!user.isActivated()) {
            log.atInfo().log(String.format("Authorization process: user with login %s has not activated account", username));
            throw new ApiRequestException(ACCOUNT_DO_NOT_ACTIVATED, HttpStatus.FORBIDDEN);
        }
        return authenticationMapper.userToUserDetails(user);
    }
}
