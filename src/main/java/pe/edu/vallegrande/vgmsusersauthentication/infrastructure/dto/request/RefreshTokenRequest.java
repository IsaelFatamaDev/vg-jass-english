package pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {

     @NotBlank(message = "El refresh token es requerido")
     private String refreshToken;
}
