package com.simbirsoft.repositories;

import com.simbirsoft.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select user.id from User user where user.login in :logins")
    List<Integer> findIdsByLogins(@Param("logins") List<String> logins);
    Optional<User> findByLogin(String login);
    Optional<User> findByActivationKey(String key);
    Optional<User> findByResetPasswordKey(String key);
    @Query("SELECT user.login FROM User user WHERE user.resetPasswordKey = :key")
    Optional<String> getLoginByResetPasswordKey(@Param("key") String key);
}
