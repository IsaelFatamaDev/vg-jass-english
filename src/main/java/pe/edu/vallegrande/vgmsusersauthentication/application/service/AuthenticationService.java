package pe.edu.vallegrande.vgmsusersauthentication.application.service;

import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.request.ChangePasswordRequest;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.request.LoginRequest;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.request.RefreshTokenRequest;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.response.AuthResponse;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.response.TokenValidationResponse;
import reactor.core.publisher.Mono;

public interface AuthenticationService {

     Mono<AuthResponse> login(LoginRequest request);

     Mono<Void> logout(String userId);

     Mono<TokenValidationResponse> validateToken(String token);

     Mono<AuthResponse> refreshToken(RefreshTokenRequest request);

     Mono<AuthResponse> getTokenByUserId(String userId);

     Mono<Void> changePassword(String userId, ChangePasswordRequest request);

     Mono<Void> resetPassword(String email);

     Mono<String> generateUserCode(String organizationId);
}
