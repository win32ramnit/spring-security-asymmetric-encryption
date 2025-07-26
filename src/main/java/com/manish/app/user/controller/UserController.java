package com.manish.app.user.controller;

import com.manish.app.config.ApiConstants;
import com.manish.app.user.entity.User;
import com.manish.app.user.service.UserService;
import com.manish.app.user.request.ChangePasswordRequest;
import com.manish.app.user.request.ProfileUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(ApiConstants.USERS_BASE)
@RequiredArgsConstructor
@Tag(name = "User", description = "User API for managing user profiles")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Update authenticated user's profile",
        description = "Updates the profile information of the authenticated user")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = ApiConstants.SUCCESS_PROFILE_UPDATED),
        @ApiResponse(responseCode = "400", description = ApiConstants.ERROR_INVALID_REQUEST),
        @ApiResponse(responseCode = "401", description = ApiConstants.ERROR_UNAUTHORIZED),
        @ApiResponse(responseCode = "404", description = ApiConstants.ERROR_USER_NOT_FOUND)
    })
    @PatchMapping(ApiConstants.USERS_ME)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void updateProfileInfo(
        @RequestBody
        @Valid
        final ProfileUpdateRequest request,
        final @AuthenticationPrincipal User user
    ) {
        log.info("Updating profile for user ID: {}", user.getId());
        this.userService.updateProfileInfo(request, user.getId());
        log.debug("Profile updated successfully for user ID: {}", user.getId());
    }


    @Operation(summary = "Change authenticated user's password", description = "Updates the password of the authenticated user")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = ApiConstants.SUCCESS_PASSWORD_CHANGED),
        @ApiResponse(responseCode = "400", description = ApiConstants.ERROR_INVALID_REQUEST),
        @ApiResponse(responseCode = "401", description = ApiConstants.ERROR_UNAUTHORIZED),
        @ApiResponse(responseCode = "404", description = ApiConstants.ERROR_USER_NOT_FOUND),
        @ApiResponse(responseCode = "403", description = ApiConstants.ERROR_FORBIDDEN_ACCESS)
    })
    @PostMapping(ApiConstants.USERS_ME_PASSWORD)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@Valid @RequestBody ChangePasswordRequest request,
        @AuthenticationPrincipal User user) {
        log.info("Changing password for user ID: {}", user.getId());
        userService.changePassword(request, user.getId());
        log.debug("Password changed successfully for user ID: {}", user.getId());
    }

    @Operation(summary = "Deactivate authenticated user's account", description = "Deactivates the authenticated user's account")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = ApiConstants.SUCCESS_ACCOUNT_DEACTIVATED),
        @ApiResponse(responseCode = "401", description = ApiConstants.ERROR_UNAUTHORIZED),
        @ApiResponse(responseCode = "404", description = ApiConstants.ERROR_USER_NOT_FOUND)
    })
    @PatchMapping(ApiConstants.USERS_ME_DEACTIVATE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateAccount(@AuthenticationPrincipal User user) {
        log.info("Deactivating account for user ID: {}", user.getId());
        userService.deactivateAccount(user.getId());
        log.debug("Account deactivated successfully for user ID: {}", user.getId());
    }

    @Operation(summary = "Reactivate authenticated user's account", description = "Reactivates the authenticated user's account")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = ApiConstants.SUCCESS_ACCOUNT_REACTIVATED),
        @ApiResponse(responseCode = "401", description = ApiConstants.ERROR_UNAUTHORIZED),
        @ApiResponse(responseCode = "404", description = ApiConstants.ERROR_USER_NOT_FOUND)
    })
    @PatchMapping(ApiConstants.USERS_ME_REACTIVATE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reactivateAccount(@AuthenticationPrincipal User user) {
        log.info("Reactivating account for user ID: {}", user.getId());
        userService.reactivateAccount(user.getId());
        log.debug("Account reactivated successfully for user ID: {}", user.getId());
    }

    @Operation(summary = "Delete authenticated user's account", description = "Permanently deletes the authenticated user's account")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = ApiConstants.SUCCESS_ACCOUNT_DELETED),
        @ApiResponse(responseCode = "401", description = ApiConstants.ERROR_UNAUTHORIZED),
        @ApiResponse(responseCode = "404", description = ApiConstants.ERROR_USER_NOT_FOUND)
    })
    @DeleteMapping(ApiConstants.USERS_ME_DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@AuthenticationPrincipal User user) {
        log.info("Deleting account for user ID: {}", user.getId());
        userService.deleteAccount(user.getId());
        log.debug("Account deleted successfully for user ID: {}", user.getId());
    }

}
