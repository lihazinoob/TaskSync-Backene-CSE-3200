package com.example.careerPilot.demo.dto;

import com.example.careerPilot.demo.entity.UserSkill;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserSkillRequest {
    @NotNull(message = "Proficiency level is required")
    private UserSkill.ProficiencyLevel proficiencyLevel;
}
