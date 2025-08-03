package com.example.careerPilot.demo.dto;

import com.example.careerPilot.demo.entity.CompanyEmployee;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InvitationRequest {
    @NotNull
    private Long invitedUserId;
    private long CompanyId;

    @NotNull
    private CompanyEmployee.Role role;
}