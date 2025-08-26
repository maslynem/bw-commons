package dy.digitalyard.commons.web.security.service;

import java.security.PublicKey;

public interface PubKeyLoader {
    PublicKey loadPubKey(String pubKeyPath);

}
