package com.example.careerPilot.demo.dto;

import com.example.careerPilot.demo.entity.UserSkill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSkillDTO {
    private Long id;
    private Long userId;
    private String username;
    private Long skillId;
    private String skillName;
    private UserSkill.ProficiencyLevel proficiencyLevel;

    public static UserSkillDTO fromEntity(UserSkill userSkill) {
        return UserSkillDTO.builder()
                .id(userSkill.getId())
                .userId(userSkill.getUser().getId())
                .username(userSkill.getUser().getUsername())
                .skillId(userSkill.getSkill().getSkillId())
                .skillName(userSkill.getSkill().getSkillName())
                .proficiencyLevel(userSkill.getProficiencyLevel())
                .build();
    }
}