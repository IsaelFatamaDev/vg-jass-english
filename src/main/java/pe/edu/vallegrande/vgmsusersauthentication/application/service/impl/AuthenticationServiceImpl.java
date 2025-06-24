package pe.edu.vallegrande.vgmsusersauthentication.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.vgmsusersauthentication.application.config.JwtConfig;
import pe.edu.vallegrande.vgmsusersauthentication.application.service.AuthenticationService;
import pe.edu.vallegrande.vgmsusersauthentication.domain.enums.StatusUsers;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.request.ChangePasswordRequest;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.request.LoginRequest;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.request.RefreshTokenRequest;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.response.AuthResponse;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.response.TokenValidationResponse;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.exception.CustomException;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.repository.AuthRepository;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.repository.UserRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;

    @Override
    public Mono<AuthResponse> login(LoginRequest request) {
        log.info("Intentando login para usuario: {}", request.getUsername());

        return authRepository.findByUsernameAndStatus(request.getUsername(), StatusUsers.ACTIVE)
                .switchIfEmpty(Mono.error(new CustomException("Credenciales inválidas", "INVALID_CREDENTIALS", 401)))
                .filter(auth -> passwordEncoder.matches(request.getPassword(), auth.getPasswordHash()))
                .switchIfEmpty(Mono.error(new CustomException("Credenciales inválidas", "INVALID_CREDENTIALS", 401)))
                .flatMap(auth ->
                    userRepository.findById(auth.getUserId())
                        .flatMap(user -> {
                            user.setLastLogin(LocalDateTime.now());
                            return userRepository.save(user)
                                    .map(savedUser -> {
                                        String accessToken = jwtConfig.generateAccessToken(auth.getUserId(),
                                                                               auth.getUsername(),
                                                                               auth.getRoles());
                                        String refreshToken = jwtConfig.generateRefreshToken(auth.getUserId());

                                        return new AuthResponse(
                                            accessToken,
                                            refreshToken,
                                            "Bearer",
                                            jwtConfig.getJwtExpiration(),
                                            auth.getUserId(),
                                            auth.getUsername(),
                                            savedUser.getPersonalInfo().getFullName(),
                                            auth.getRoles()
                                        );
                                    });
                        })
                )
                .doOnSuccess(response -> log.info("Login exitoso para usuario: {}", response.getUsername()))
                .doOnError(error -> log.error("Login fallido para usuario: {}", request.getUsername()));
    }

    @Override
    public Mono<Void> logout(String userId) {
        log.info("Cerrando sesión para usuario: {}", userId);
        return Mono.<Void>fromRunnable(() -> log.info("Sesión cerrada para usuario: {}", userId))
                .then();
    }

    @Override
    public Mono<TokenValidationResponse> validateToken(String token) {
        log.info("Validando token");

        try {
            if (jwtConfig.isTokenValid(token)) {
                String userId = jwtConfig.extractUserId(token);
                String username = jwtConfig.extractUsername(token);
                return Mono.just(new TokenValidationResponse(true, userId, username, "Token válido"));
            } else {
                return Mono.just(new TokenValidationResponse(false, null, null, "Token inválido"));
            }
        } catch (Exception e) {
            log.error("Validación de token fallida: {}", e.getMessage());
            return Mono.just(new TokenValidationResponse(false, null, null, "Token inválido"));
        }
    }

    @Override
    public Mono<AuthResponse> refreshToken(RefreshTokenRequest request) {
        log.info("Actualizando token");

        try {
            if (!jwtConfig.isRefreshTokenValid(request.getRefreshToken())) {
                return Mono.error(new CustomException("Token de actualización inválido", "INVALID_REFRESH_TOKEN", 401));
            }

            String userId = jwtConfig.extractUserId(request.getRefreshToken());

            return authRepository.findByUserId(userId)
                    .switchIfEmpty(Mono.error(new CustomException("Usuario no encontrado", "USER_NOT_FOUND", 404)))
                    .flatMap(auth ->
                        userRepository.findById(auth.getUserId())
                            .map(user -> {
                                String newAccessToken = jwtConfig.generateAccessToken(auth.getUserId(),
                                                                          auth.getUsername(),
                                                                          auth.getRoles());
                                String newRefreshToken = jwtConfig.generateRefreshToken(auth.getUserId());

                                return new AuthResponse(
                                    newAccessToken,
                                    newRefreshToken,
                                    "Bearer",
                                    jwtConfig.getJwtExpiration(),
                                    auth.getUserId(),
                                    auth.getUsername(),
                                    user.getPersonalInfo().getFullName(),
                                    auth.getRoles()
                                );
                            })
                    )
                    .doOnSuccess(response -> log.info("Token actualizado para usuario: {}", response.getUsername()))
                    .doOnError(error -> log.error("Actualización de token fallida: {}", error.getMessage()));
        } catch (Exception e) {
            return Mono.error(new CustomException("Token de actualización inválido", "INVALID_REFRESH_TOKEN", 401));
        }
    }

    @Override
    public Mono<AuthResponse> getTokenByUserId(String userId) {
        log.info("Obteniendo token para usuario: {}", userId);

        return authRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new CustomException("Usuario no encontrado", "USER_NOT_FOUND", 404)))
                .flatMap(auth ->
                    userRepository.findById(auth.getUserId())
                        .map(user -> {
                            String accessToken = jwtConfig.generateAccessToken(auth.getUserId(),
                                                                   auth.getUsername(),
                                                                   auth.getRoles());
                            String refreshToken = jwtConfig.generateRefreshToken(auth.getUserId());

                            return new AuthResponse(
                                accessToken,
                                refreshToken,
                                "Bearer",
                                jwtConfig.getJwtExpiration(),
                                auth.getUserId(),
                                auth.getUsername(),
                                user.getPersonalInfo().getFullName(),
                                auth.getRoles()
                            );
                        })
                );
    }

    @Override
    public Mono<Void> changePassword(String userId, ChangePasswordRequest request) {
        log.info("Cambiando contraseña para usuario: {}", userId);

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return Mono.error(new CustomException("Las contraseñas no coinciden", "PASSWORDS_DONT_MATCH", 400));
        }

        return authRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new CustomException("Usuario no encontrado", "USER_NOT_FOUND", 404)))
                .filter(auth -> passwordEncoder.matches(request.getCurrentPassword(), auth.getPasswordHash()))
                .switchIfEmpty(Mono.error(new CustomException("Contraseña actual incorrecta", "INVALID_CURRENT_PASSWORD", 400)))
                .flatMap(auth -> {
                    auth.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
                    return authRepository.save(auth);
                })
                .then()
                .doOnSuccess(v -> log.info("Contraseña cambiada exitosamente para usuario: {}", userId))
                .doOnError(error -> log.error("Cambio de contraseña fallido para usuario: {}", userId));
    }

    @Override
    public Mono<Void> resetPassword(String email) {
        log.info("Restableciendo contraseña para email: {}", email);

        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new CustomException("Usuario no encontrado con el email proporcionado", "USER_NOT_FOUND", 404)))
                .flatMap(user ->
                    authRepository.findByUserId(user.getId())
                        .flatMap(auth -> {
                            String temporaryPassword = generateTemporaryPassword();
                            auth.setPasswordHash(passwordEncoder.encode(temporaryPassword));

                            log.info("Contraseña temporal generada para usuario {}: {}", user.getId(), temporaryPassword);

                            return authRepository.save(auth);
                        })
                )
                .then()
                .doOnSuccess(v -> log.info("Restablecimiento de contraseña exitoso para email: {}", email))
                .doOnError(error -> log.error("Restablecimiento de contraseña fallido para email: {}", email));
    }

    @Override
    public Mono<String> generateUserCode(String organizationId) {
        String baseCode = "USR-" + organizationId + "-";
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return Mono.just(baseCode + randomSuffix);
    }

    private String generateTemporaryPassword() {
        return UUID.randomUUID().toString().substring(0, 12);
    }
}
