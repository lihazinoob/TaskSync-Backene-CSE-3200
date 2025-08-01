package com.example.careerPilot.demo.dto;

import com.example.careerPilot.demo.entity.JobSkill;
import lombok.Data;

@Data
public class JobSkillDTO {
    private SkillDTO skill;
    private JobSkill.ProficiencyLevel proficiencyLevel;

    public static JobSkillDTO fromEntity(JobSkill jobSkill) {
        JobSkillDTO dto = new JobSkillDTO();
        dto.setSkill(SkillDTO.fromEntity(jobSkill.getSkill()));
        dto.setProficiencyLevel(jobSkill.getProficiencyLevel());
        return dto;
    }
}