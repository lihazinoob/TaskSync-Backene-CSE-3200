package com.example.careerPilot.demo.repository;

import com.example.careerPilot.demo.entity.Company;
import com.example.careerPilot.demo.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByCompany(Company company);
}
