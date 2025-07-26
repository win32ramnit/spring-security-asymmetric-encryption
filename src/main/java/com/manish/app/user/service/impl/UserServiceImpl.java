package com.manish.app.user.service.impl;

import com.manish.app.exception.BusinessException;
import com.manish.app.exception.ErrorCode;
import com.manish.app.role.Role;
import com.manish.app.role.RoleRepository;
import com.manish.app.user.entity.User;
import com.manish.app.user.mapper.UserMapper;
import com.manish.app.user.repository.UserRepository;
import com.manish.app.user.service.UserService;
import com.manish.app.user.request.ChangePasswordRequest;
import com.manish.app.user.request.ProfileUpdateRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(final String userEmail) throws UsernameNotFoundException {
        return this.userRepository.findByEmailIgnoreCase(userEmail)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with userEmail: " + userEmail));
    }

    @Override
    @Transactional
    public void updateProfileInfo(final ProfileUpdateRequest request, String userId) {
        final User saveduser = this.userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));

        this.userMapper.mergerUserInfo(saveduser, request);
        this.userRepository.save(saveduser);
    }

    @Override
    @Transactional
    public void changePassword(final ChangePasswordRequest request, final String userId) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException(ErrorCode.CHANGE_PASSWORD_MISMATCH);
        }
        final User savedUser = this.userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));
        if (this.passwordEncoder.matches(request.getCurrentPassword(),
            savedUser.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CURRENT_PASSWORD);
        }

        final String encoded = this.passwordEncoder.encode(request.getNewPassword());
        savedUser.setPassword(encoded);
        this.userRepository.save(savedUser);
    }

    @Override
    @Transactional
    public void deactivateAccount(final String userId) {
        final User user = this.userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));

        if (!user.isEnabled()) {
            throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_DEACTIVATED, userId);
        }
        user.setEnabled(false);
        this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void reactivateAccount(final String userId) {
        final User user = this.userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));

        if (user.isEnabled()) {
            throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_ACTIVATED, userId);
        }
        user.setEnabled(true);
        this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteAccount(final String userId) {
        // this method need to set of the entities
        // the logic is just to schedule a profile for deletion
        // and a scheduled job will pick the profile and delete everything
        User user = this.userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));

        user.setMarkedForDeletion(true);
        user.setMarkedAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("User {} marked for deletion", userId);
    }


    @Transactional
    public void processScheduledDeletions() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(1); // e.g., delay before deletion

        List<User> usersToDelete = userRepository.findAllByMarkedForDeletionTrueAndMarkedAtBefore(cutoff);
        final int batchSize = 50;
        int counter = 0;

        for (User user : usersToDelete) {
            log.info("Deleting user: {}", user.getId());

            // Break associations
            for (Role role : new HashSet<>(user.getRoles())) {
                user.removeRole(role);
            }
            userRepository.delete(user);

            counter++;
            if (counter % batchSize == 0) {
                userRepository.flush();
            }
        }

        log.info("Processed {} deletions", usersToDelete.size());
    }
}
