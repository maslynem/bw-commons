package ru.boardworld.commons.web.security.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public interface HttpSecurityCustomizer {
    HttpSecurity customize(HttpSecurity http);
}
