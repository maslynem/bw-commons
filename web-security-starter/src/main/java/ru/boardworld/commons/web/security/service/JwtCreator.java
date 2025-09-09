package ru.boardworld.commons.web.security.service;

import ru.boardworld.commons.web.security.model.user.AuthenticatedUser;


public interface JwtCreator {
    String generateAccessToken(AuthenticatedUser authenticatedUser);

    String generateRefreshToken(AuthenticatedUser authenticatedUser);

}
