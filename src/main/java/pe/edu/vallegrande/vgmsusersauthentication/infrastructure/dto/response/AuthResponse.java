package pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.vallegrande.vgmsusersauthentication.domain.enums.RolesUsers;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
     private String accessToken;
     private String refreshToken;
     private String tokenType = "Bearer";
     private Long expiresIn;
     private String userId;
     private String username;
     private String fullName;
     private List<RolesUsers> roles;
}
