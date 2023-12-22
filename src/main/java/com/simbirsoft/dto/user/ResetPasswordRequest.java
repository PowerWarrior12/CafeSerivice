package com.simbirsoft.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import static com.simbirsoft.constants.ErrorMessages.INCORRECT_PASSWORD;

@Getter
public class ResetPasswordRequest {
    private final String password;
    private final String password2;

    public ResetPasswordRequest(
            @JsonProperty("password")
            @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = INCORRECT_PASSWORD)
            String password,
            @JsonProperty("repeat_password")
            String password2) {
        this.password = password;
        this.password2 = password2;
    }
}
