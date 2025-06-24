package pe.edu.vallegrande.vgmsusersauthentication.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pe.edu.vallegrande.vgmsusersauthentication.domain.enums.StatusUsers;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String userCode;
    private String organizationId;
    private PersonalInfo personalInfo;
    private Contact contact;
    private List<WaterBoxAssignment> waterBoxes;
    private StatusUsers status;
    private LocalDateTime registrationDate;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
