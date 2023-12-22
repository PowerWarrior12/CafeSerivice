package com.simbirsoft.repositories;

import com.simbirsoft.DBTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DBTest
@Sql(value = {"/sql/generate-users.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/sql/users-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("get login by reset password key if exists")
    void getLoginByResetPasswordKeyIfExists() {
        String actualLogin = userRepository.getLoginByResetPasswordKey("541243").orElse(null);
        String expectedLogin = "test3@mail.ru";
        assertEquals(expectedLogin, actualLogin);
    }

    @Test
    @DisplayName("get login by reset password key if not exists")
    void getLoginByResetPasswordKeyIfNotExists() {
        String actualLogin = userRepository.getLoginByResetPasswordKey("431241").orElse(null);
        assertNull(actualLogin);
    }
}