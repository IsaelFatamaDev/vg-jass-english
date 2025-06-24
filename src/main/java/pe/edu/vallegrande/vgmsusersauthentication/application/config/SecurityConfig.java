package pe.edu.vallegrande.vgmsusersauthentication.application.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Configuración de seguridad para producción
     */
    @Bean
    @Profile("prod")
    public SecurityWebFilterChain prodSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "/",
                                "/favicon.ico",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/v3/api-docs/**",
                                "/api-docs/**",
                                "/actuator/**",
                                "/api/v1/health",
                                "/api/v1/auth/login",
                                "/api/v1/auth/reset-password",
                                "/api/v1/auth/validate",
                                "/api/v1/auth/refresh",
                                "/api/v1/auth/generate-user-code",
                                "/api/v1/users/change-password")
                        .permitAll()
                        .pathMatchers("/api/v1/users/**")
                        .authenticated()
                        .anyExchange().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    /**
     * Configuración de seguridad para desarrollo (más permisiva pero con CORS)
     */
    @Bean
    @Profile({"dev", "test", "local"})
    public SecurityWebFilterChain devSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .authorizeExchange(exchanges -> exchanges
                        // En desarrollo, permitir todas las rutas
                        .anyExchange().permitAll())
                .build();
    }

    /**
     * Configuración CORS para permitir peticiones desde el frontend
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:4200",
                "http://localhost:3000",
                "https://localhost:4200",
                "https://localhost:3000",
                "http://127.0.0.1:4200",
                "https://*.jassdigital.com",
                "https://*.vallegrande.edu.pe"
        ));

        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"
        ));

        configuration.setAllowedHeaders(Arrays.asList(
                "Origin",
                "Content-Type",
                "Accept",
                "Authorization",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers",
                "X-Requested-With",
                "Accept-Language",
                "Accept-Encoding",
                "X-Organization-Id",
                "X-User-Id",
                "X-Username",
                "Cache-Control"
        ));

        configuration.setExposedHeaders(Arrays.asList(
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials",
                "Authorization",
                "Content-Disposition",
                "X-Total-Count",
                "X-Page-Number",
                "X-Page-Size"
        ));

        configuration.setAllowCredentials(true);

        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}