package com.example.careerPilot.demo.repository;

import com.example.careerPilot.demo.entity.Skill;
import com.example.careerPilot.demo.entity.User;
import com.example.careerPilot.demo.entity.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {
    List<UserSkill> findByUser(User user);
    Optional<UserSkill> findByUserAndSkill(User user, Skill skill);
}
