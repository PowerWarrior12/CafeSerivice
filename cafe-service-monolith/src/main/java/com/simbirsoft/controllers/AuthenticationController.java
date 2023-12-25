package com.simbirsoft.controllers;

import com.simbirsoft.dto.authentication.AuthenticationRequest;
import com.simbirsoft.dto.authentication.AuthenticationResponse;
import com.simbirsoft.dto.user.RegistrationRequest;
import com.simbirsoft.dto.user.RegistrationResponse;
import com.simbirsoft.dto.user.ResetPasswordRequest;
import com.simbirsoft.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody @Valid RegistrationRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }
    @GetMapping("/forgot/{login}")
    public ResponseEntity<String> forgotPassword(@PathVariable("login") String login) {
        return ResponseEntity.ok(authenticationService.sendResetPasswordCode(login));
    }
    @PostMapping("/reset/{resetKey}")
    public ResponseEntity<String> resetPassword(
            @PathVariable("resetKey") String resetKey,
            @RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(authenticationService.resetPassword(resetKey, request));
    }
    @PatchMapping("/activate/{activationKey}")
    public ResponseEntity<String> activateAccount(
            @PathVariable("activationKey") String activationKey
    ) {
        return ResponseEntity.ok(authenticationService.activate(activationKey));
    }
}
