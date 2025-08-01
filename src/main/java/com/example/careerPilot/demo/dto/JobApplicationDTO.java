package com.example.careerPilot.demo.dto;

import com.example.careerPilot.demo.entity.JobApplication;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobApplicationDTO {
    private Long id;
    private Long userId;
    private String userName;
    private JobApplication.Status status;
    private LocalDateTime appliedAt;
    private LocalDateTime processedAt;

    public static JobApplicationDTO fromEntity(JobApplication application) {
        JobApplicationDTO dto = new JobApplicationDTO();
        dto.setId(application.getId());
        dto.setUserId(application.getApplicant().getId());
        dto.setUserName(application.getApplicant().getUsername());
        dto.setStatus(application.getStatus());
        dto.setAppliedAt(application.getAppliedAt());
        dto.setProcessedAt(application.getProcessedAt());
        return dto;
    }
}