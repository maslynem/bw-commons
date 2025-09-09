package ru.boardworld.commons.web.security.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class AccessTokenDto {
    private String token;
    private OffsetDateTime createdAt;
    private OffsetDateTime expiresAt;
}
