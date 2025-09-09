package ru.boardworld.commons.web.security.service.impl;

import ru.boardworld.commons.web.security.service.PrivateKeyLoader;
import ru.boardworld.commons.web.security.service.PublicKeyLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
public class PemKeyLoader implements PublicKeyLoader, PrivateKeyLoader {
    private final ResourceLoader resourceLoader;

    @Override
    public PublicKey loadPublicKey(String path) {
        try {
            Resource resource = resourceLoader.getResource(path);
            if (!resource.exists()) throw new IllegalArgumentException("Key not found: " + path);

            String pem = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8)
                    .replaceAll("\\r?\\n", "")
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .trim();

            byte[] keyBytes = Base64.getDecoder().decode(pem);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to load public key from PEM", ex);
        }
    }

    @Override
    public PrivateKey loadPrivateKey(String path) {
        try {
            Resource resource = resourceLoader.getResource(path);
            if (!resource.exists()) throw new IllegalArgumentException("Key not found: " + path);

            String pem = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8)
                    .replaceAll("\\r?\\n", "")
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .trim();

            byte[] keyBytes = Base64.getDecoder().decode(pem);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to load private key from PEM", ex);
        }
    }
}
