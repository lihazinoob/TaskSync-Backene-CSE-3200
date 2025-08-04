package com.example.careerPilot.demo.dto;


import com.example.careerPilot.demo.entity.Company;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.example.careerPilot.demo.entity.Project;

@Data
@Builder
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private int noOfEmployees;
    private BigDecimal completion;
    private Long createdById;
    private String status;
    private int totalTasks;
    private int tasksCompleted;
    private long companyId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProjectDTO fromEntity(Project project) {
        if (project == null) return null;
        return ProjectDTO.builder()
                .id(project.getProjectId())
                .name(project.getName())
                .description(project.getDescription())
                .noOfEmployees(project.getNoOfEmployees())
                .completion(project.getCompletion())
                .createdById(project.getCreatedBy() != null ? project.getCreatedBy().getId() : null)
                .status(project.getStatus() != null ? project.getStatus().name() : null)
                .totalTasks(project.getTotalTasks())
                .tasksCompleted(project.getTasksCompleted())
                .companyId(project.getCompany().getId())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .deadline(project.getDeadline())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}