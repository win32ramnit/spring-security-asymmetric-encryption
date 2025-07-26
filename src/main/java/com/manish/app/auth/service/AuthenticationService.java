package com.manish.app.auth.service;

import com.manish.app.auth.request.AuthenticationRequest;
import com.manish.app.auth.request.RefreshRequest;
import com.manish.app.auth.request.RegistrationRequest;
import com.manish.app.auth.response.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse login(AuthenticationRequest request);

    void register(RegistrationRequest request);

    AuthenticationResponse refreshToken(RefreshRequest request);

}
