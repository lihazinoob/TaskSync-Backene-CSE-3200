package com.example.careerPilot.demo.repository;

import com.example.careerPilot.demo.entity.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectUserRepository extends JpaRepository<ProjectUser , Long> {

    Optional<ProjectUser> findByProject_ProjectIdAndUser_Id(Long projectId, Long userId);
    void deleteByProject_ProjectIdAndUser_Id(Long projectId, Long userId);
}
