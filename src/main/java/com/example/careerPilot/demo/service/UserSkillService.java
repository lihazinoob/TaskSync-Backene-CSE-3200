package com.example.careerPilot.demo.service;

import com.example.careerPilot.demo.entity.Skill;
import com.example.careerPilot.demo.entity.User;
import com.example.careerPilot.demo.entity.UserSkill;
import com.example.careerPilot.demo.repository.UserSkillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSkillService {

    private final UserSkillRepository userSkillRepository;
    private final UserService userService;
    private final SkillService skillService;

    public List<UserSkill> getUserSkillsByUsername(String username) {
        log.debug("Fetching skills for user: {}", username);
        User user = userService.getUserByUsername(username);
        return userSkillRepository.findByUser(user);
    }

    @Transactional
    public UserSkill addSkillToUser(String username, Long skillId, UserSkill.ProficiencyLevel proficiencyLevel) {
        log.debug("Adding skill id: {} with proficiency: {} to user: {}", skillId, proficiencyLevel, username);

        User user = userService.getUserByUsername(username);
        Skill skill = skillService.getSkillById(skillId);

        // Check if the user already has this skill
        if (userSkillRepository.findByUserAndSkill(user, skill).isPresent()) {
            throw new RuntimeException("User already has this skill");
        }

        UserSkill userSkill = UserSkill.builder()
                .user(user)
                .skill(skill)
                .proficiencyLevel(proficiencyLevel)
                .build();

        return userSkillRepository.save(userSkill);
    }

    @Transactional
    public UserSkill updateUserSkill(String username, Long userSkillId, UserSkill.ProficiencyLevel proficiencyLevel) {
        log.debug("Updating skill id: {} with proficiency: {} for user: {}", userSkillId, proficiencyLevel, username);

        User user = userService.getUserByUsername(username);
        UserSkill userSkill = userSkillRepository.findById(userSkillId)
                .orElseThrow(() -> new RuntimeException("User skill not found with id: " + userSkillId));

        // Ensure the user owns this skill
        if (!userSkill.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("User does not have permission to update this skill");
        }

        userSkill.setProficiencyLevel(proficiencyLevel);
        return userSkillRepository.save(userSkill);
    }

    @Transactional
    public void deleteUserSkill(String username, Long userSkillId) {
        log.debug("Deleting skill id: {} for user: {}", userSkillId, username);

        User user = userService.getUserByUsername(username);
        UserSkill userSkill = userSkillRepository.findById(userSkillId)
                .orElseThrow(() -> new RuntimeException("User skill not found with id: " + userSkillId));

        // Ensure the user owns this skill
        if (!userSkill.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("User does not have permission to delete this skill");
        }

        userSkillRepository.delete(userSkill);
    }
}