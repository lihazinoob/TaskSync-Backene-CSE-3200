package com.example.careerPilot.demo.repository;

import com.example.careerPilot.demo.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    Optional<Skill> findBySkillNameIgnoreCase(String skillName);
    List<Skill> findBySkillNameContainingIgnoreCase(String skillName);
    List<Skill> findAllBySkillIdIn(Set<Long> skillIds);
}