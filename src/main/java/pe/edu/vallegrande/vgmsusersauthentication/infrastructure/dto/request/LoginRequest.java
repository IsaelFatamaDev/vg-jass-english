package pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

     @NotBlank(message = "El nombre de usuario es requerido")
     private String username;

     @NotBlank(message = "La contraseña es requerida")
     private String password;
}
