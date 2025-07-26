package com.manish.app.security;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Configuration properties for JWT authentication, including key paths and algorithm.
 */
@Component
@Getter
@ToString(exclude = {"privateKeyPath", "publicKeyPath"}) // Exclude sensitive fields from toString
@Validated
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private static final List<String> SUPPORTED_ALGORITHMS = List.of("RSA", "EC");

    /**
     * Path to the private key resource (e.g., classpath:/keys/private_key.pem).
     */
    @NotBlank(message = "Private key path must not be blank")
    private String privateKeyPath;

    /**
     * Path to the public key resource (e.g., classpath:/keys/public_key.pem).
     */
    @NotBlank(message = "Public key path must not be blank")
    private String publicKeyPath;

    /**
     * Key algorithm (e.g., RSA, EC). Default is RSA.
     */
    @NotBlank(message = "Algorithm must not be blank")
    @Pattern(regexp = "RSA|EC", message = "Algorithm must be one of: RSA or EC")
    private String algorithm = "RSA";

    /**
     * Validates properties after binding.
     */
    @PostConstruct
    public void validate() {
        if (!SUPPORTED_ALGORITHMS.contains(algorithm)) {
            throw new IllegalArgumentException(
                "Unsupported JWT algorithm: " + algorithm + ". Supported: " + SUPPORTED_ALGORITHMS);
        }
    }

    // Setters are package-private to restrict modifications to within the package
    void setPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }

    void setPublicKeyPath(String publicKeyPath) {
        this.publicKeyPath = publicKeyPath;
    }

    void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}
