package com.manish.app.auth.request;

import com.manish.app.validation.NonDisposableEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequest {

    @NotBlank(message = "VALIDATION.REGISTRATION.FIRSTNAME.NOT_BLANK")
    @Size(min = 3, max = 50, message = "VALIDATION.REGISTRATION.FIRSTNAME.SIZE")
    @Pattern(regexp = "^[\\p{Lu}]\\p{Ll}+$", message = "VALIDATION.REGISTRATION.FIRSTNAME.PATTERN")
    @Schema(example = "John", description = "First name")
    private String firstName;

    @NotBlank(message = "VALIDATION.REGISTRATION.LASTNAME.NOT_BLANK")
    @Size(min = 2, max = 50, message = "VALIDATION.REGISTRATION.LASTNAME.SIZE")
    @Pattern(regexp = "^[\\p{Lu}]\\p{Ll}+$", message = "VALIDATION.REGISTRATION.LASTNAME.PATTERN")
    @Schema(example = "Doe", description = "Last name")
    private String lastName;

    @NotBlank(message = "VALIDATION.REGISTRATION.EMAIL.NOT_BLANK")
    @Email(message = "VALIDATION.REGISTRATION.EMAIL.FORMAT"  )
    @NonDisposableEmail(message = "VALIDATION.REGISTRATION.EMAIL.DISPOSABLE")
    @Schema(example = "john@mail.com", description = "Email address")
    private String email;

    @NotBlank(message = "VALIDATION.REGISTRATION.PHONE.NOT_BLANK")
    @Pattern(regexp = "^\\+?[0-9]{10,13}$", message = "VALIDATION.REGISTRATION.PHONE.FORMAT")
    @Schema(example = "+913344556677", description = "Phone number")
    private String phoneNumber;

    @NotBlank(message = "VALIDATION.REGISTRATION.PASSWORD.NOT_BLANK")
    @Size(min = 8, max = 72, message = "VALIDATION.REGISTRATION.PASSWORD.SIZE")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*\\W).*$", message = "VALIDATION.REGISTRATION.PASSWORD.PATTERN")
    @Schema(example = "Password123!", description = "Password")
    private String password;

    @NotBlank(message = "VALIDATION.REGISTRATION.CONFIRM_PASSWORD.NOT_BLANK")
    @Size(min = 8, max = 72, message = "VALIDATION.REGISTRATION.CONFIRM_PASSWORD.SIZE")
    @Schema(example = "Password123!", description = "Confirm Password")
    private String confirmPassword;
}

