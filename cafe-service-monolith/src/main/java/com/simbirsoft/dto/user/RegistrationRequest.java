package com.simbirsoft.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import static com.simbirsoft.constants.ErrorMessages.*;

@Getter
public class RegistrationRequest {
    private final String login;
    private final String firstName;
    private final String lastName;
    private final String password;
    private final String password2;
    private final String phoneNumber;
    private final String address;

    public RegistrationRequest(
            @JsonProperty("login")
            @NotBlank(message = EMPTY_LOGIN)
            @Email(message = INCORRECT_LOGIN)
            String login,
            @JsonProperty("first_name") @NotBlank(message = EMPTY_FIRST_NAME)
            String firstName,
            @JsonProperty("last_name") @NotBlank(message = EMPTY_LAST_NAME)
            String lastName,
            @JsonProperty("password")
            @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = INCORRECT_PASSWORD)
            String password,
            @JsonProperty("repeat_password")
            String password2,
            @JsonProperty("phone_number")
            @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")
            String phoneNumber,
            @JsonProperty("address")
            String address
    ) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.password2 = password2;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}
