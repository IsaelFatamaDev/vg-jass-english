package pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {

     @NotBlank(message = "La contraseña actual es requerida")
     private String currentPassword;
     @NotBlank(message = "La nueva contraseña es requerida")
     @Size(min = 8, message = "La nueva contraseña debe tener al menos 8 caracteres")
     @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "La nueva contraseña debe contener al menos una mayúscula, una minúscula, un número y un carácter especial")
     private String newPassword;

     @NotBlank(message = "La confirmación de contraseña es requerida")
     private String confirmPassword;
}
