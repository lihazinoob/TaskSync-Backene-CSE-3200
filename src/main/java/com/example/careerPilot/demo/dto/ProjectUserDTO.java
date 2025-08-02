package com.example.careerPilot.demo.dto;

import com.example.careerPilot.demo.entity.ProjectUser;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProjectUserDTO {
    private Long id;
    private Long projectId;
    private Long userId;
    private String role;
    private String status;
    private LocalDateTime joinedAt;

    public static ProjectUserDTO fromEntity(ProjectUser projectUser) {
        if (projectUser == null) return null;
        return ProjectUserDTO.builder()
                .id(projectUser.getId())
                .projectId(projectUser.getProject() != null ? projectUser.getProject().getProjectId() : null)
                .userId(projectUser.getUser() != null ? projectUser.getUser().getId() : null)
                .role(projectUser.getRole() != null ? projectUser.getRole().name() : null)
                .status(projectUser.getStatus() != null ? projectUser.getStatus().name() : null)
                .joinedAt(projectUser.getJoinedAt())
                .build();
    }
}