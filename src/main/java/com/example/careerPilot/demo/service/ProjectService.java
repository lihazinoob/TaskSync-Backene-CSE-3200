package com.example.careerPilot.demo.service;

import com.example.careerPilot.demo.dto.ProjectDTO;
import com.example.careerPilot.demo.entity.Project;
import com.example.careerPilot.demo.entity.ProjectUser;
import com.example.careerPilot.demo.entity.User;
import com.example.careerPilot.demo.repository.ProjectUserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.example.careerPilot.demo.repository.ProjectRepository;
import com.example.careerPilot.demo.repository.userRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final userRepository userRepository;
    private final ProjectUserRepository projectUserRepository;

    public ProjectDTO createProject(@Valid ProjectDTO projectDTO, UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found with username: " + userDetails.getUsername()));
        Project project = Project.builder()
                .name(projectDTO.getName())
                .description(projectDTO.getDescription())
                .createdBy(user)
                .noOfEmployees(projectDTO.getNoOfEmployees())
                .completion(projectDTO.getCompletion())
                .status(Project.ProjectStatus.valueOf(projectDTO.getStatus() != null ? projectDTO.getStatus() : "IN_PROGRESS"))
                .totalTasks(projectDTO.getTotalTasks())
                .tasksCompleted(projectDTO.getTasksCompleted())
                .startDate(projectDTO.getStartDate())
                .endDate(projectDTO.getEndDate())
                .deadline(projectDTO.getDeadline())
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now())
                .build();
        Project savedProject = projectRepository.save(project);
        ProjectUser projectUser = ProjectUser.builder()
                .project(savedProject)
                .user(user)
                .role(Project.ProjectRole.CREATOR)
                .status(ProjectUser.RequestStatus.APPROVED)
                .joinedAt(java.time.LocalDateTime.now())
                .build();
        projectUserRepository.save(projectUser);
        return ProjectDTO.fromEntity(savedProject);
    }

    public Page<ProjectDTO> getAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(ProjectDTO::fromEntity);
    }

    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
        return ProjectDTO.fromEntity(project);
    }

    public ProjectDTO updateProject(Long id, @Valid ProjectDTO projectDTO, UserDetails userDetails) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
        if (!project.getCreatedBy().getUsername().equals(userDetails.getUsername())) {
            throw new RuntimeException("You don't have permission to update this project");
        }
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setNoOfEmployees(projectDTO.getNoOfEmployees());
        project.setCompletion(projectDTO.getCompletion());
        project.setStatus(Project.ProjectStatus.valueOf(projectDTO.getStatus() != null ? projectDTO.getStatus() : "IN_PROGRESS"));
        project.setTotalTasks(projectDTO.getTotalTasks());
        project.setTasksCompleted(projectDTO.getTasksCompleted());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        project.setDeadline(projectDTO.getDeadline());
        project.setUpdatedAt(LocalDateTime.now());
        Project updatedProject = projectRepository.save(project);
        return ProjectDTO.fromEntity(updatedProject);
    }

    public void deleteProject(Long id, UserDetails userDetails) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
        if (!project.getCreatedBy().getUsername().equals(userDetails.getUsername())) {
            throw new RuntimeException("You don't have permission to delete this project");
        }
        projectRepository.delete(project);
    }
}
