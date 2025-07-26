package com.manish.app.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileUpdateRequest {

    @NotBlank(message = "VALIDATION.REGISTRATION.FIRSTNAME.NOT_BLANK")
    @Size(min = 5, max = 50, message = "VALIDATION.REGISTRATION.FIRSTNAME.SIZE")
    @Pattern(regexp = "^[\\p{Lu}]\\p{Ll}+$", message = "VALIDATION.REGISTRATION.FIRSTNAME.PATTERN")
    @Schema(example = "John", description = "First name")
    private String firstName;

    @NotBlank(message = "VALIDATION.REGISTRATION.LASTNAME.NOT_BLANK")
    @Size(min = 5, max = 50, message = "VALIDATION.REGISTRATION.LASTNAME.SIZE")
    @Pattern(regexp = "^[\\p{Lu}]\\p{Ll}+$", message = "VALIDATION.REGISTRATION.LASTNAME.PATTERN")
    @Schema(example = "Doe", description = "Last name")
    private String lastName;

    @NotNull(message = "VALIDATION.REGISTRATION.DATE_OF_BIRTH.NOT_NULL")
    @Past(message = "VALIDATION.REGISTRATION.DATE_OF_BIRTH.PAST")
    @Schema(example = "1990-01-01", format = "yyyy-MM-dd", description = "Date of birth")
    private LocalDate dateOfBirth;

}
