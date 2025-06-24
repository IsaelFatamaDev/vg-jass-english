package pe.edu.vallegrande.vgmsusersauthentication.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Configuración global de CORS para WebFlux
 * Esta configuración trabaja junto con SecurityConfig
 */
@Configuration
public class CorsConfig {

     @Bean
     public CorsWebFilter corsWebFilter() {
          CorsConfiguration corsConfiguration = new CorsConfiguration();

          corsConfiguration.setAllowedOriginPatterns(Arrays.asList(
                  "http://localhost:4200",
                  "http://localhost:3000",
                  "https://localhost:4200",
                  "http://127.0.0.1:4200",
                  "https://localhost:3000",
                  "https://*.jassdigital.com",
                  "https://*.vallegrande.edu.pe"
          ));

          corsConfiguration.setAllowedMethods(Arrays.asList(
                  "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"
          ));

          corsConfiguration.setAllowedHeaders(Arrays.asList(
                  "Origin", "Content-Type", "Accept", "Authorization",
                  "Access-Control-Request-Method", "Access-Control-Request-Headers",
                  "X-Requested-With", "Accept-Language", "Accept-Encoding",
                  "X-Organization-Id", "X-User-Id", "X-Username", "Cache-Control"
          ));

          corsConfiguration.setExposedHeaders(Arrays.asList(
                  "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials",
                  "Authorization", "Content-Disposition", "X-Total-Count",
                  "X-Page-Number", "X-Page-Size"
          ));

          corsConfiguration.setAllowCredentials(true);

          corsConfiguration.setMaxAge(3600L);

          UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
          source.registerCorsConfiguration("/**", corsConfiguration);

          return new CorsWebFilter(source);
     }
}