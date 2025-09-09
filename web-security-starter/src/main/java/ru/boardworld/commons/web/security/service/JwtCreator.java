package ru.boardworld.commons.web.security.service;

import ru.boardworld.commons.web.security.dto.AccessTokenDto;
import ru.boardworld.commons.web.security.dto.RefreshTokenDto;
import ru.boardworld.commons.web.security.model.user.AuthenticatedUser;


public interface JwtCreator {
    AccessTokenDto generateAccessToken(AuthenticatedUser authenticatedUser);

    RefreshTokenDto generateRefreshToken(AuthenticatedUser authenticatedUser);

}
