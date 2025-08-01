package com.example.careerPilot.demo.dto;

import com.example.careerPilot.demo.entity.JobPost;
import com.example.careerPilot.demo.entity.JobSkill;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class JobPostRequest {
    @NotBlank(message = "Job title is required")
    private String jobTitle;

    @NotBlank(message = "Job description is required")
    private String jobDescription;

    private String requirements;

    @PositiveOrZero(message = "Salary must be positive")
    private int lowerSalary;

    @PositiveOrZero(message = "Salary must be positive")
    private int upperSalary;

    private String location;

    @NotNull(message = "Job type is required")
    private JobPost.JobType jobType;

    private String jobCategory;

    @Future(message = "Application deadline must be in the future")
    private LocalDate applicationDeadline;

    @NotEmpty(message = "At least one skill is required")
    private List<JobSkillRequest> skills;
}