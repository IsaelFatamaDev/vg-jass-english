package pe.edu.vallegrande.vgmsusersauthentication.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.vallegrande.vgmsusersauthentication.domain.enums.AssignmentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaterBoxAssignment {
    private String waterBoxId;
    private String waterBoxCode;
    private AssignmentStatus assignmentStatus;
    private BigDecimal monthlyFee;
    private LocalDateTime assignmentDate;
}
