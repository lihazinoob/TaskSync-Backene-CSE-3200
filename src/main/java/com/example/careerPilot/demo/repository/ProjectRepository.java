package com.example.careerPilot.demo.repository;

import com.example.careerPilot.demo.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
