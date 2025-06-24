package pe.edu.vallegrande.vgmsusersauthentication.application.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI/Swagger para la documentación de la API
 */
@Configuration
public class OpenApiConfig {

     @Value("${app.name}")
     private String appName;

     @Value("${app.description}")
     private String appDescription;

     @Value("${app.version:2.0.0}")
     private String appVersion;

     @Value("${app.organization:Universidad Valle Grande}")
     private String organization;

     @Bean
     public OpenAPI customOpenAPI() {
          return new OpenAPI()
                    .info(new Info()
                              .title(appName)
                              .description(appDescription)
                              .version(appVersion)
                              .contact(new Contact()
                                        .name(organization)
                                        .email("soporte@vallegrande.edu.pe")
                                        .url("https://vallegrande.edu.pe"))
                              .license(new License()
                                        .name("MIT License")
                                        .url("https://opensource.org/licenses/MIT")))
                    .servers(List.of(
                              new Server()
                                        .url("http://localhost:8080")
                                        .description("Servidor de Desarrollo"),
                              new Server()
                                        .url("https://api.jassdigital.com")
                                        .description("Servidor de Producción")));
     }
}
