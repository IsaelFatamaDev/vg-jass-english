package pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenValidationResponse {
     private boolean valid;
     private String userId;
     private String username;
     private String message;
}
