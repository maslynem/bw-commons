package dy.digitalyard.commons.web.security.service.impl;

import dy.digitalyard.commons.web.security.service.PubKeyLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

@Slf4j
@RequiredArgsConstructor
public class X509PubKeyLoader implements PubKeyLoader {
    private final ResourceLoader resourceLoader;

    @Override
    public PublicKey loadPubKey(String pubKeyPath) {
        try {
            log.debug("Loading public key from {}...", pubKeyPath);
            Resource pubKeyResource = resourceLoader.getResource(pubKeyPath);

            if (!pubKeyResource.exists()) {
                throw new RuntimeException("Resource not found: " + pubKeyPath);
            }

            try (InputStream pubKeyInputStream = pubKeyResource.getInputStream()) {
                CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
                Certificate certificate = certFactory.generateCertificate(pubKeyInputStream);
                PublicKey pubKey = certificate.getPublicKey();
                log.debug("Public key loaded: {}", pubKey.getAlgorithm());
                return pubKey;
            }
        } catch (Exception e) {
            throw new RuntimeException("JWT public key is not readable", e);
        }
    }
}
