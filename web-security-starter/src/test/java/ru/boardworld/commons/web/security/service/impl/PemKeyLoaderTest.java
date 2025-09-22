package ru.boardworld.commons.web.security.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;

import java.security.PrivateKey;
import java.security.PublicKey;

import static org.assertj.core.api.Assertions.assertThat;

class PemKeyLoaderTest {

    @Test
    void loadPublicKey_fromClasspath() {
        DefaultResourceLoader loader = new DefaultResourceLoader();
        PemKeyLoader keyLoader = new PemKeyLoader(loader);

        PublicKey key = keyLoader.loadPublicKey("classpath:test-public.key");

        assertThat(key).isNotNull();
        assertThat(key.getAlgorithm()).isNotBlank();
    }
    @Test
    void loadPrivateKey_fromClasspath() {
        DefaultResourceLoader loader = new DefaultResourceLoader();
        PemKeyLoader keyLoader = new PemKeyLoader(loader);

        PrivateKey key = keyLoader.loadPrivateKey("classpath:test-private.key");

        assertThat(key).isNotNull();
        assertThat(key.getAlgorithm()).isNotBlank();
    }
}
