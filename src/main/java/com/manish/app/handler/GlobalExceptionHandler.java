package com.manish.app.handler;

import com.manish.app.exception.BusinessException;
import com.manish.app.exception.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleException(final BusinessException e) {
        final ErrorResponse body = ErrorResponse.builder()
            .code(e.getErrorCode().getCode())
            .message(e.getMessage())
            .build();
        log.info("Business exception occurred: {}", e.getMessage());
        log.debug(e.getMessage(), e);

        return ResponseEntity
            .status(e.getErrorCode()
                .getStatus() != null ? e.getErrorCode().getStatus() : HttpStatus.BAD_REQUEST)
            .body(body);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleException(final DisabledException e) {
        final ErrorResponse body = ErrorResponse.builder()
            .code(ErrorCode.ERR_USER_DISABLED.getCode())
            .message(ErrorCode.ERR_USER_DISABLED.getDefaultMessage())
            .build();
        log.info("Disabled account exception occurred: {}", e.getMessage());
        log.debug(e.getMessage(), e);

        return ResponseEntity
           .status(HttpStatus.UNAUTHORIZED)
           .body(body);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleException(final BadCredentialsException e) {
        log.info("Bad credentials exception occurred: {}", e.getMessage());
        log.debug(e.getMessage(), e);
        final ErrorResponse body = ErrorResponse.builder()
            .code(ErrorCode.BAD_CREDENTIALS.getCode())
            .message(ErrorCode.BAD_CREDENTIALS.getDefaultMessage())
            .build();

        return ResponseEntity
           .status(HttpStatus.UNAUTHORIZED)
           .body(body);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(final UsernameNotFoundException e) {
        final ErrorResponse body = ErrorResponse.builder()
            .code(ErrorCode.USER_NOT_FOUND.getCode())
            .message(ErrorCode.USER_NOT_FOUND.getDefaultMessage())
            .build();
        log.info("User not found exception occurred: {}", e.getMessage());
        log.debug(e.getMessage(), e);

        return ResponseEntity
           .status(HttpStatus.NOT_FOUND)
           .body(body);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(final EntityNotFoundException e) {
        final ErrorResponse body = ErrorResponse.builder()
            .code(ErrorCode.USER_NOT_FOUND.getCode())
            .message(ErrorCode.USER_NOT_FOUND.getDefaultMessage())
            .build();
        log.info("Entity not found exception occurred: {}", e.getMessage());
        log.debug(e.getMessage(), e);

        return ResponseEntity
           .status(HttpStatus.NOT_FOUND)
           .body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(final MethodArgumentNotValidException e) {
        final List<ErrorResponse.ValidationError> errors = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            errors.add(ErrorResponse.ValidationError.builder()
               .field(((FieldError) error).getField())
                .code(error.getDefaultMessage())
               .message(error.getDefaultMessage())
               .build());
        });

        final ErrorResponse body = ErrorResponse.builder()
            .validationErrors(errors)
            .build();
        log.info("Validation exception occurred: {}", e.getMessage());
        log.debug(e.getMessage(), e);

        return ResponseEntity
           .status(HttpStatus.BAD_REQUEST)
           .body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception e) {
        final ErrorResponse body = ErrorResponse.builder()
            .code(ErrorCode.INTERNAL_ERROR.getCode())
            .message(ErrorCode.INTERNAL_ERROR.getDefaultMessage())
            .build();
        log.error("An unexpected error occurred: {}", e.getMessage());
        log.error(e.getMessage(), e);

        return ResponseEntity
           .status(HttpStatus.INTERNAL_SERVER_ERROR)
           .body(body);
    }

}
