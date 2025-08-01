package com.example.careerPilot.demo.service;

import com.example.careerPilot.demo.entity.Skill;
import com.example.careerPilot.demo.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;

    public List<Skill> getAllSkills() {
        log.debug("Fetching all skills");
        return skillRepository.findAll();
    }

    public Skill getSkillById(Long id) {
        log.debug("Fetching skill with id: {}", id);
        return skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found with id: " + id));
    }

    @Transactional
    public Skill createSkill(String skillName) {
        log.debug("Creating new skill with name: {}", skillName);

        // Check if skill with the same name already exists
        if (skillRepository.findBySkillNameIgnoreCase(skillName).isPresent()) {
            throw new RuntimeException("Skill with name '" + skillName + "' already exists");
        }

        Skill skill = Skill.builder()
                .skillName(skillName)
                .build();

        return skillRepository.save(skill);
    }

    @Transactional
    public Skill updateSkill(Long id, String skillName) {
        log.debug("Updating skill with id: {} to name: {}", id, skillName);

        Skill skill = getSkillById(id);

        // Check if new skill name already exists for another skill
        skillRepository.findBySkillNameIgnoreCase(skillName)
                .ifPresent(existingSkill -> {
                    if (!existingSkill.getSkillId().equals(id)) {
                        throw new RuntimeException("Skill with name '" + skillName + "' already exists");
                    }
                });

        skill.setSkillName(skillName);
        return skillRepository.save(skill);
    }

    @Transactional
    public void deleteSkill(Long id) {
        log.debug("Deleting skill with id: {}", id);

        Skill skill = getSkillById(id);

        // Check if skill is associated with any users or jobs
        if (!skill.getUserSkills().isEmpty()) {
            throw new RuntimeException("Cannot delete skill as it is associated with users");
        }

        if (!skill.getJobSkills().isEmpty()) {
            throw new RuntimeException("Cannot delete skill as it is associated with jobs");
        }

        skillRepository.delete(skill);
    }

    public List<Skill> searchSkillsByName(String name) {
        log.debug("Searching skills with name containing: {}", name);
        return skillRepository.findBySkillNameContainingIgnoreCase(name);
    }
}