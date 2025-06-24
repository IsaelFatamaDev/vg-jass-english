package pe.edu.vallegrande.vgmsusersauthentication.infrastructure.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.vallegrande.vgmsusersauthentication.domain.enums.AssignmentStatus;
import pe.edu.vallegrande.vgmsusersauthentication.domain.enums.DocumentType;
import pe.edu.vallegrande.vgmsusersauthentication.domain.enums.RolesUsers;
import pe.edu.vallegrande.vgmsusersauthentication.domain.enums.StatusUsers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWithWaterBoxesResponse {
     private String id;
     private String userCode;
     private String organizationId;

     private DocumentType documentType;
     private String documentNumber;
     private String firstName;
     private String lastName;
     private String fullName;

     private String email;
     private String phone;

     private String streetAddress;
     private String streetId;
     private String zoneId;

     private StatusUsers status;
     private LocalDateTime registrationDate;
     private LocalDateTime lastLogin;
     private LocalDateTime createdAt;
     private LocalDateTime updatedAt;

     private List<RolesUsers> roles;
     private String username;

     private List<WaterBoxInfo> waterBoxes;

     @Data
     @NoArgsConstructor
     @AllArgsConstructor
     public static class WaterBoxInfo {
          private String waterBoxId;
          private String waterBoxCode;
          private String waterBoxName;
          private String location;
          private AssignmentStatus assignmentStatus;
          private BigDecimal monthlyFee;
          private LocalDateTime assignmentDate;
     }
}
