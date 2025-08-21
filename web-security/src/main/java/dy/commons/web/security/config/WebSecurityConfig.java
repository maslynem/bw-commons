package dy.commons.web.security.config;

import dy.commons.web.security.auth.ForbiddenEntryPoint;
import dy.commons.web.security.auth.JwtAuthenticationEntryPoint;
import dy.commons.web.security.auth.JwtAuthenticationTokenFilter;
import dy.commons.web.security.config.model.AuthConfig;
import dy.commons.web.security.config.model.PermissionUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final ForbiddenEntryPoint forbiddenEntryPoint;
    private final AuthenticationProvider jwtAuthenticationProvider;
    private final CorsConfigurationSource corsConfigurationSource;
    private final PermissionUrl permissionUrl;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Отключение ненужных функций
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // 2. Настройка CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // 3. Настройка сессий
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 4. Настройка авторизации
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(permissionUrl.getPermissions()).permitAll()
                        .anyRequest().authenticated()
                )

                // 5. Добавление кастомного провайдера
                .authenticationProvider(jwtAuthenticationProvider)

                // 6. Добавление кастомного фильтра
                .addFilterBefore(
                        new JwtAuthenticationTokenFilter(authenticationManager(), jwtAuthenticationEntryPoint),
                        UsernamePasswordAuthenticationFilter.class
                )

                // 7. Настройка обработки исключений
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(forbiddenEntryPoint)
                )

                // 8. Настройка security headers (после исключений)
                .headers(Customizer.withDefaults());
        return http.build();
    }
}