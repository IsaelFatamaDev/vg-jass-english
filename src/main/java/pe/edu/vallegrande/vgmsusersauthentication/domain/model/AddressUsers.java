package pe.edu.vallegrande.vgmsusersauthentication.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressUsers {
    private String streetAddress;
    private String streetId;
    private String zoneId;
}
