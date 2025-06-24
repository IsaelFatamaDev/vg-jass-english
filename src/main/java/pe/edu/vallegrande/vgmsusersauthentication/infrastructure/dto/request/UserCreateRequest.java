package pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.vallegrande.vgmsusersauthentication.domain.enums.DocumentType;
import pe.edu.vallegrande.vgmsusersauthentication.domain.enums.RolesUsers;

import jakarta.validation.constraints.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {

     @NotBlank(message = "El código de organización es requerido")
     private String organizationId;

     @NotNull(message = "El tipo de documento es requerido")
     private DocumentType documentType;

     @NotBlank(message = "El número de documento es requerido")
     @Pattern(regexp = "^[0-9]{8,12}$", message = "El número de documento debe tener entre 8 y 12 dígitos")
     private String documentNumber;

     @NotBlank(message = "El nombre es requerido")
     @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
     private String firstName;

     @NotBlank(message = "El apellido es requerido")
     @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
     private String lastName;

     @NotBlank(message = "El email es requerido")
     @Email(message = "El formato del email no es válido")
     private String email;

     @Pattern(regexp = "^[+]?[0-9]{9,15}$", message = "El formato del teléfono no es válido")
     private String phone;

     private String streetAddress;
     private String streetId;
     private String zoneId;

     @NotBlank(message = "El nombre de usuario es requerido")
     @Size(min = 4, max = 20, message = "El nombre de usuario debe tener entre 4 y 20 caracteres")
     private String username;

     @NotBlank(message = "La contraseña es requerida")
     @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
     @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_])[\\s\\S]+$",
             message = "La contraseña debe contener al menos una mayúscula, una minúscula, un número y un carácter especial")
     private String password;

     @NotEmpty(message = "Los roles son requeridos")
     private List<RolesUsers> roles;
}
