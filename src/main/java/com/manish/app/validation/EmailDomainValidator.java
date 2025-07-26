package com.manish.app.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EmailDomainValidator implements ConstraintValidator<NonDisposableEmail, String> {

    private final Set<String> blockedDomains;

    public EmailDomainValidator(@Value("${app.security.disposable-emails}") final List<String> domains) {
        this.blockedDomains = domains.stream()
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(final String email, final ConstraintValidatorContext constraintValidatorContext) {
        if (email == null || !email.contains("@")) {
            return true;
        }
        final int atIndex = email.indexOf("@") + 1;
        final int dotIndex = email.lastIndexOf(".");
        final String domain = email.substring(atIndex, dotIndex);
        return !this.blockedDomains.contains(domain.toLowerCase());
    }
}
