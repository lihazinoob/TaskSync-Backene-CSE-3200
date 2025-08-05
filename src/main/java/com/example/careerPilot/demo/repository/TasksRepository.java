package com.example.careerPilot.demo.repository;

import com.example.careerPilot.demo.entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TasksRepository extends JpaRepository<Tasks , Long> {
    int countByProject_ProjectId(Long projectId);
    List<Tasks> findByProject_ProjectId(Long projectId);
}
