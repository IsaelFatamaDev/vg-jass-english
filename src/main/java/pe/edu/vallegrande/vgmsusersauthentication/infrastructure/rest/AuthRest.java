package pe.edu.vallegrande.vgmsusersauthentication.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.vgmsusersauthentication.application.service.AuthenticationService;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ResponseDto;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.request.ChangePasswordRequest;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.request.LoginRequest;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.request.RefreshTokenRequest;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.response.AuthResponse;
import pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.response.TokenValidationResponse;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthRest {

     private final AuthenticationService authenticationService;

     @PostMapping("/login")
     public Mono<ResponseEntity<ResponseDto<AuthResponse>>> login(@Valid @RequestBody LoginRequest request) {
          log.info("Login attempt for username: {}", request.getUsername());

          return authenticationService.login(request)
                    .map(authResponse -> ResponseEntity.ok(new ResponseDto<>(true, authResponse)))
                    .onErrorResume(error -> {
                         log.error("Login failed: {}", error.getMessage());
                         return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                   .body(new ResponseDto<AuthResponse>(false,
                                             new pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ErrorMessage(
                                                       error.getMessage(), "LOGIN_FAILED",
                                                       HttpStatus.UNAUTHORIZED.value()))));
                    });
     }

     @PostMapping("/logout/{userId}")
     public Mono<ResponseEntity<ResponseDto<String>>> logout(@PathVariable String userId) {
          log.info("Logout request for user: {}", userId);

          return authenticationService.logout(userId)
                    .then(Mono.just(ResponseEntity.ok(ResponseDto.success("Logout exitoso"))))
                    .onErrorResume(error -> {
                         log.error("Logout failed: {}", error.getMessage());
                         return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                   .body(new ResponseDto<String>(false,
                                             new pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ErrorMessage(
                                                       error.getMessage(), "LOGOUT_FAILED",
                                                       HttpStatus.BAD_REQUEST.value()))));
                    });
     }

     @PostMapping("/validate")
     public Mono<ResponseEntity<ResponseDto<TokenValidationResponse>>> validateToken(@RequestParam String token) {
          log.info("Token validation request");

          return authenticationService.validateToken(token)
                    .map(validationResponse -> ResponseEntity.ok(new ResponseDto<>(true, validationResponse)))
                    .onErrorResume(error -> {
                         log.error("Token validation failed: {}", error.getMessage());
                         return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                   .body(new ResponseDto<TokenValidationResponse>(false,
                                             new pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ErrorMessage(
                                                       error.getMessage(), "TOKEN_VALIDATION_FAILED",
                                                       HttpStatus.UNAUTHORIZED.value()))));
                    });
     }

     @PostMapping("/refresh")
     public Mono<ResponseEntity<ResponseDto<AuthResponse>>> refreshToken(
               @Valid @RequestBody RefreshTokenRequest request) {
          log.info("Token refresh request");

          return authenticationService.refreshToken(request)
                    .map(authResponse -> ResponseEntity.ok(new ResponseDto<>(true, authResponse)))
                    .onErrorResume(error -> {
                         log.error("Token refresh failed: {}", error.getMessage());
                         return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                   .body(new ResponseDto<AuthResponse>(false,
                                             new pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ErrorMessage(
                                                       error.getMessage(), "TOKEN_REFRESH_FAILED",
                                                       HttpStatus.UNAUTHORIZED.value()))));
                    });
     }

     @GetMapping("/token/user/{userId}")
     public Mono<ResponseEntity<ResponseDto<AuthResponse>>> getTokenByUserId(@PathVariable String userId) {
          log.info("Get token request for user: {}", userId);

          return authenticationService.getTokenByUserId(userId)
                    .map(authResponse -> ResponseEntity.ok(new ResponseDto<>(true, authResponse)))
                    .onErrorResume(error -> {
                         log.error("Get token failed: {}", error.getMessage());
                         return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                   .body(new ResponseDto<AuthResponse>(false,
                                             new pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ErrorMessage(
                                                       error.getMessage(), "TOKEN_GENERATION_FAILED",
                                                       HttpStatus.NOT_FOUND.value()))));
                    });
     }

     @PostMapping("/change-password/{userId}")
     public Mono<ResponseEntity<ResponseDto<String>>> changePassword(
               @PathVariable String userId,
               @Valid @RequestBody ChangePasswordRequest request) {
          log.info("Change password request for user: {}", userId);

          return authenticationService.changePassword(userId, request)
                    .then(Mono.just(ResponseEntity.ok(ResponseDto.success("Contraseña cambiada exitosamente"))))
                    .onErrorResume(error -> {
                         log.error("Change password failed: {}", error.getMessage());
                         return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                   .body(new ResponseDto<String>(false,
                                             new pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ErrorMessage(
                                                       error.getMessage(), "PASSWORD_CHANGE_FAILED",
                                                       HttpStatus.BAD_REQUEST.value()))));
                    });
     }

     @PostMapping("/reset-password")
     public Mono<ResponseEntity<ResponseDto<String>>> resetPassword(@RequestParam String email) {
          log.info("Reset password request for email: {}", email);

          return authenticationService.resetPassword(email)
                    .then(Mono.just(ResponseEntity.ok(ResponseDto.success("Contraseña temporal enviada al email"))))
                    .onErrorResume(error -> {
                         log.error("Reset password failed: {}", error.getMessage());
                         return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                   .body(new ResponseDto<String>(false,
                                             new pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ErrorMessage(
                                                       error.getMessage(), "PASSWORD_RESET_FAILED",
                                                       HttpStatus.NOT_FOUND.value()))));
                    });
     }

     @GetMapping("/generate-user-code/{organizationId}")
     public Mono<ResponseEntity<ResponseDto<String>>> generateUserCode(@PathVariable String organizationId) {
          log.info("Generate user code request for organization: {}", organizationId);

          return authenticationService.generateUserCode(organizationId)
                    .map(userCode -> ResponseEntity.ok(new ResponseDto<>(true, userCode)))
                    .onErrorResume(error -> {
                         log.error("Generate user code failed: {}", error.getMessage());
                         return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                   .body(new ResponseDto<String>(false,
                                             new pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.ErrorMessage(
                                                       error.getMessage(), "USER_CODE_GENERATION_FAILED",
                                                       HttpStatus.INTERNAL_SERVER_ERROR.value()))));
                    });
     }
}
