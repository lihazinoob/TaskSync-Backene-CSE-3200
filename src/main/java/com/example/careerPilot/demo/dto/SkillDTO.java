package com.example.careerPilot.demo.dto;

import com.example.careerPilot.demo.entity.Skill;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillDTO {
    private Long skillId;

    @NotBlank(message = "Skill name is required")
    private String skillName;

    public static SkillDTO fromEntity(Skill skill) {
        return SkillDTO.builder()
                .skillId(skill.getSkillId())
                .skillName(skill.getSkillName())
                .build();
    }
}