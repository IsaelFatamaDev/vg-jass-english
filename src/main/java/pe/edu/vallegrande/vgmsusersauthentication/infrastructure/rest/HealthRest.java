package pe.edu.vallegrande.vgmsusersauthentication.infrastructure.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Controlador para verificar el estado de salud de la aplicación
 */
@RestController
@RequestMapping("/api/v1/health")
@Tag(name = "Health Check", description = "Endpoints para verificar el estado de la aplicación")
public class HealthRest {

     @GetMapping
     @Operation(summary = "Verificar estado de la aplicación", description = "Retorna el estado actual de la aplicación y información básica")
     @ApiResponses(value = {
               @ApiResponse(responseCode = "200", description = "Aplicación funcionando correctamente"),
               @ApiResponse(responseCode = "503", description = "Servicio no disponible")
     })
     public Mono<ResponseEntity<Map<String, Object>>> health() {
          return Mono.just(ResponseEntity.ok(Map.of(
                    "status", "UP",
                    "timestamp", LocalDateTime.now(),
                    "service", "vg-ms-users-authentication",
                    "version", "2.0.0",
                    "message", "Microservicio de Usuarios y Autenticación funcionando correctamente")));
     }
}
