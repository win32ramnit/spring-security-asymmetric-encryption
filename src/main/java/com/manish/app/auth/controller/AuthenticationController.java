package com.manish.app.auth.controller;

import com.manish.app.auth.request.AuthenticationRequest;
import com.manish.app.auth.request.RefreshRequest;
import com.manish.app.auth.request.RegistrationRequest;
import com.manish.app.auth.response.AuthenticationResponse;
import com.manish.app.auth.service.AuthenticationService;
import com.manish.app.config.ApiConstants;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.AUTH_BASE)
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API for user authentication and authorization")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping(ApiConstants.AUTH_LOGIN)
    public ResponseEntity<AuthenticationResponse> login(
        @Valid
        @RequestBody
        final AuthenticationRequest request) {
        return ResponseEntity.ok(this.authenticationService.login(request));
    }

    @PostMapping(ApiConstants.AUTH_REGISTER)
    public ResponseEntity<Void> register(
        @Valid
        @RequestBody
        final RegistrationRequest request) {
        this.authenticationService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(ApiConstants.AUTH_REFRESH)
    public ResponseEntity<AuthenticationResponse> refreshToken(
        @Valid
        @RequestBody
        final RefreshRequest request) {
        return ResponseEntity.ok(this.authenticationService.refreshToken(request));
    }


}
