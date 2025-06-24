package pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.vallegrande.vgmsusersauthentication.domain.enums.DocumentType;

import jakarta.validation.constraints.*;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

     private Optional<DocumentType> documentType;

     @Pattern(regexp = "^[0-9]{8,12}$", message = "El número de documento debe tener entre 8 y 12 dígitos")
     private Optional<String> documentNumber;

     @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
     private Optional<String> firstName;

     @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
     private Optional<String> lastName;

     @Email(message = "El formato del email no es válido")
     private Optional<String> email;

     @Pattern(regexp = "^[+]?[0-9]{9,15}$", message = "El formato del teléfono no es válido")
     private Optional<String> phone;
     private Optional<String> streetAddress;
     private Optional<String> streetId;
     private Optional<String> zoneId;
}
