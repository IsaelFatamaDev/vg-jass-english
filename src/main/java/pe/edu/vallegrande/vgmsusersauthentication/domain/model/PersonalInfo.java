package pe.edu.vallegrande.vgmsusersauthentication.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.vallegrande.vgmsusersauthentication.domain.enums.DocumentType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalInfo {
    private DocumentType documentType;
    private String documentNumber;
    private String firstName;
    private String lastName;
    private String fullName;
}
