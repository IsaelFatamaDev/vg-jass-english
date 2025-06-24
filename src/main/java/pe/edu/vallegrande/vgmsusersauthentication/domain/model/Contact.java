package pe.edu.vallegrande.vgmsusersauthentication.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
    private String phone;
    private String email;
    private AddressUsers address;
}