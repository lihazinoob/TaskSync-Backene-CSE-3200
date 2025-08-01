package com.example.careerPilot.demo.dto;

import com.example.careerPilot.demo.entity.CompanyEmployee;
import com.example.careerPilot.demo.entity.Invitation;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InvitationDTO {
    private Long id;
    private Long companyId;
    private String companyName;
    private String invitedUserUsername;
    private String inviterUsername;
    private CompanyEmployee.Role role;
    private Invitation.InvitationStatus status;
    private LocalDateTime createdAt;

    public static InvitationDTO fromEntity(Invitation invitation) {
        InvitationDTO dto = new InvitationDTO();
        dto.setId(invitation.getId());
        dto.setCompanyId(invitation.getCompany().getId());
        dto.setCompanyName(invitation.getCompany().getCompanyName());
        dto.setInvitedUserUsername(invitation.getInvitedUser().getUsername());
        dto.setInviterUsername(invitation.getInviter().getUsername());
        dto.setRole(invitation.getRole());
        dto.setStatus(invitation.getStatus());
        dto.setCreatedAt(invitation.getCreatedAt());
        return dto;
    }
}