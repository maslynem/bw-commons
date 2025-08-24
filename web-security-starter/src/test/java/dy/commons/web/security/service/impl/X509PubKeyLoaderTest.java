package dy.commons.web.security.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;

import java.security.PublicKey;

import static org.assertj.core.api.Assertions.assertThat;

class X509PubKeyLoaderTest {

    @Test
    void loadPublicKey_fromClasspathCertificate() {
        DefaultResourceLoader loader = new DefaultResourceLoader();
        X509PubKeyLoader keyLoader = new X509PubKeyLoader(loader);

        PublicKey key = keyLoader.loadPubKey("classpath:test-public-key.crt");

        assertThat(key).isNotNull();
        assertThat(key.getAlgorithm()).isNotBlank();
    }
}
