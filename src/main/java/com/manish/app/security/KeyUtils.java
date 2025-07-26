package com.manish.app.security;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Utility class for loading cryptographic keys from PEM files for JWT authentication.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KeyUtils {

    private final JwtProperties jwtProperties;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        try {
            this.privateKey = loadPrivateKey(jwtProperties.getPrivateKeyPath());
            validateKeyStrength(privateKey);
            this.publicKey = loadPublicKey(jwtProperties.getPublicKeyPath());
            validateKeyStrength(publicKey);
            log.info("Successfully loaded {} keys from {} and {}", jwtProperties.getAlgorithm(),
                jwtProperties.getPrivateKeyPath(), jwtProperties.getPublicKeyPath());
        } catch (IOException e) {
            log.error("I/O error while loading keys from {} or {}", jwtProperties.getPrivateKeyPath(),
                jwtProperties.getPublicKeyPath(), e);
            throw new IllegalStateException("Failed to load keys due to I/O error", e);
        } catch (NoSuchAlgorithmException e) {
            log.error("Unsupported algorithm: {}", jwtProperties.getAlgorithm(), e);
            throw new IllegalStateException("Failed to load keys due to unsupported algorithm: " + jwtProperties.getAlgorithm(), e);
        } catch (InvalidKeySpecException e) {
            log.error("Invalid key specification for {} keys", jwtProperties.getAlgorithm(), e);
            throw new IllegalStateException("Failed to load keys due to invalid key specification", e);
        } catch (IllegalArgumentException e) {
            log.error("Invalid PEM format or missing key markers in {} or {}", jwtProperties.getPrivateKeyPath(),
                jwtProperties.getPublicKeyPath(), e);
            throw new IllegalStateException("Failed to load keys due to malformed key files", e);
        }
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public String getAlgorithm() {
        return jwtProperties.getAlgorithm();
    }

    public void reloadKeys() {
        init();
        log.info("Keys reloaded successfully");
    }

    private PrivateKey loadPrivateKey(String pemPath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String key = extractKeyFromPEM(pemPath, "PRIVATE KEY");
        byte[] decoded = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        return KeyFactory.getInstance(jwtProperties.getAlgorithm()).generatePrivate(spec);
    }

    private PublicKey loadPublicKey(String pemPath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String key = extractKeyFromPEM(pemPath, "PUBLIC KEY");
        byte[] decoded = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        return KeyFactory.getInstance(jwtProperties.getAlgorithm()).generatePublic(spec);
    }

    private String extractKeyFromPEM(String path, String keyType) throws IOException {
        try (InputStream is = KeyUtils.class.getResourceAsStream(path)) {
            if (is == null) {
                throw new IllegalArgumentException("Key file not found: " + path);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                StringBuilder keyBuilder = new StringBuilder();
                String line;
                String beginMarker = "-----BEGIN " + keyType + "-----";
                String endMarker = "-----END " + keyType + "-----";
                while ((line = reader.readLine()) != null) {
                    if (line.contains(beginMarker)) {
                        continue;
                    }
                    if (line.contains(endMarker)) {
                        break;
                    }
                    keyBuilder.append(line);
                }
                String key = keyBuilder.toString();
                if (key.isEmpty()) {
                    throw new IllegalArgumentException("Invalid PEM format for " + keyType + ": " + path);
                }
                return key;
            }
        }
    }

    private void validateKeyStrength(Object key) {
        if (key instanceof java.security.interfaces.RSAKey) {
            int keySize = ((java.security.interfaces.RSAKey) key).getModulus().bitLength();
            if (keySize < 2048) {
                throw new IllegalArgumentException("Key size too weak: " + keySize + " bits. Minimum required: 2048 bits");
            }
        }
        // Add validation for EC keys if needed
    }
}
