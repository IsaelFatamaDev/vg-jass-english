package pe.edu.vallegrande.vgmsusersauthentication.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Configuración de seguridad simplificada para desarrollo
 */
@Configuration
@EnableWebFluxSecurity
@Profile("simple")
public class SimpleSecurityConfig {

    @Bean
    public SecurityWebFilterChain simpleSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/**")
                        .permitAll())
                .build();
    }
}
