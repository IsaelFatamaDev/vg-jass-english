package pe.edu.vallegrande.vgmsusersauthentication.application.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtConfig jwtConfig;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();

        try {
            if (jwtConfig.isTokenValid(authToken)) {
                String username = jwtConfig.extractUsername(authToken);
                String userId = jwtConfig.extractUserId(authToken);
                List<String> roles = jwtConfig.extractRoles(authToken);

                List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

                auth.setDetails(userId);

                log.debug("JWT authentication successful for user: {}", username);
                return Mono.just(auth);
            } else {
                log.debug("JWT token is invalid");
                return Mono.empty();
            }
        } catch (Exception e) {
            log.error("JWT authentication failed: {}", e.getMessage());
            return Mono.empty();
        }
    }
}
