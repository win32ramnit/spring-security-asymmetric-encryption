package com.manish.app.auth.service.impl;

import com.manish.app.auth.request.AuthenticationRequest;
import com.manish.app.auth.request.RefreshRequest;
import com.manish.app.auth.request.RegistrationRequest;
import com.manish.app.auth.response.AuthenticationResponse;
import com.manish.app.auth.service.AuthenticationService;
import com.manish.app.exception.BusinessException;
import com.manish.app.exception.ErrorCode;
import com.manish.app.role.Role;
import com.manish.app.role.RoleRepository;
import com.manish.app.security.JwtService;
import com.manish.app.user.entity.User;
import com.manish.app.user.mapper.UserMapper;
import com.manish.app.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;


    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        final Authentication auth = this.authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        final User user = (User) auth.getPrincipal();
        final String accessToken = jwtService.generateAccessToken(user.getUsername());
        final String refreshToken = jwtService.generateRefreshToken(user.getUsername());
        final String tokenType = "Bearer";

        return AuthenticationResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .tokenType(tokenType)
            .build();
    }

    @Override
    @Transactional
    public void register(RegistrationRequest request) {
        // validate the request data
        checkUserEmail(request.getEmail());
        checkUserPhoneNumber(request.getPhoneNumber());
        checkPasswords(request.getPassword(), request.getConfirmPassword());

        final Role userRole = this.roleRepository.findByName("ROLE_USER")
            .orElseThrow(() -> new EntityNotFoundException("Role user does not exist"));

        final Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        final User user = this.userMapper.toUser(request);
        user.setRoles(roles);
        log.debug("Saving user: {}", user);
        this.userRepository.save(user);

//        final List<User> users = List.of(user);
//        userRole.setUsers(users);
//
//        this.roleRepository.save(userRole);

    }

    @Override
    public AuthenticationResponse refreshToken(RefreshRequest request) {
        final String newAccessToken = jwtService.refreshAccessToken(request.getRefreshToken());
        final String tokenType = "Bearer";
        return AuthenticationResponse.builder()
            .accessToken(newAccessToken)
            .refreshToken(request.getRefreshToken())
            .tokenType(tokenType)
            .build();
    }

    private void checkUserEmail(final String email) {
        final boolean emailExists = userRepository.existsByEmailIgnoreCase(email);
        if (emailExists) {
            log.error("Email already exists: {}", email);
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    private void checkUserPhoneNumber(final String phoneNumber) {
        final boolean phoneNumberExists = userRepository.existsByPhoneNumber(phoneNumber);
        if (phoneNumberExists) {
            log.error("Phone number already exists: {}", phoneNumber);
            throw new BusinessException(ErrorCode.PHONE_ALREADY_EXISTS);
        }
    }

    private void checkPasswords(final String password, final String confirmPassword) {
        if (password == null || !password.equals(confirmPassword)) {
            log.error("New password and confirm password are not the same");
            throw new BusinessException(ErrorCode.PASSWORD_MISMATCH);
        }
    }

}
