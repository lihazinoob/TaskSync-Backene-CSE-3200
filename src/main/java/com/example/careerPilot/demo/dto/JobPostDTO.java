package com.example.careerPilot.demo.dto;

import com.example.careerPilot.demo.entity.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class JobPostDTO {
    private Long id;
    private String jobTitle;
    private String jobDescription;
    private String requirements;
    private int lowerSalary;
    private int upperSalary;
    private String location;
    private JobPost.JobType jobType;
    private String jobCategory;
    private LocalDate applicationDeadline;
    private JobPost.Status status;
    private LocalDateTime createdAt;
    private List<JobSkillDTO> skills;

    public static JobPostDTO fromEntity(JobPost jobPost) {
        JobPostDTO dto = new JobPostDTO();
        dto.setId(jobPost.getId());
        dto.setJobTitle(jobPost.getJobTitle());
        dto.setJobDescription(jobPost.getJobDescription());
        dto.setRequirements(jobPost.getRequirements());
        dto.setLowerSalary(jobPost.getLowerSalary());
        dto.setUpperSalary(jobPost.getUpperSalary());
        dto.setLocation(jobPost.getLocation());
        dto.setJobType(jobPost.getJobType());
        dto.setJobCategory(jobPost.getJobCategory());
        dto.setApplicationDeadline(jobPost.getApplicationDeadline());
        dto.setStatus(jobPost.getStatus());
        dto.setCreatedAt(jobPost.getCreatedAt());
//        dto.setSkills(jobPost.getJobSkills().stream()
//                .map(JobSkillDTO::fromEntity)
//                .collect(Collectors.toList()));
        if(jobPost.getJobSkills() != null) {
            dto.setSkills(jobPost.getJobSkills().stream()
                    .map(JobSkillDTO::fromEntity)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}