package com.example.careerPilot.demo.dto;

import com.example.careerPilot.demo.entity.JobSkill;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JobSkillRequest {
    @NotNull(message = "Skill ID is required")
    private Long skillId;

    @NotNull(message = "Proficiency level is required")
    private JobSkill.ProficiencyLevel proficiencyLevel;
}