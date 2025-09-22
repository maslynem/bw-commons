package ru.boardworld.commons.web.security.service;

import java.security.PrivateKey;

public interface PrivateKeyLoader {
    PrivateKey loadPrivateKey(String privateKeyPath);
}
