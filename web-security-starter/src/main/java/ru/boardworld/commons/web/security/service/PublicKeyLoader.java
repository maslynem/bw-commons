package ru.boardworld.commons.web.security.service;

import java.security.PublicKey;

public interface PublicKeyLoader {
    PublicKey loadPublicKey(String pubKeyPath);
}
