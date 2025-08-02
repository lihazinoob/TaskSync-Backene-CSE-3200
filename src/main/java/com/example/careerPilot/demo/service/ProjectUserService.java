package com.example.careerPilot.demo.service;

import com.example.careerPilot.demo.dto.ProjectUserDTO;
import com.example.careerPilot.demo.entity.Project;
import com.example.careerPilot.demo.entity.ProjectUser;
import com.example.careerPilot.demo.entity.User;
import com.example.careerPilot.demo.repository.ProjectRepository;
import com.example.careerPilot.demo.repository.ProjectUserRepository;
import com.example.careerPilot.demo.repository.userRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProjectUserService {
    private final ProjectUserRepository projectUserRepository;
    private final ProjectRepository projectRepository;
    private final userRepository userRepository;

    public ProjectUserDTO addUserToProject(ProjectUserDTO dto, UserDetails userDetails) {
        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Only project owner/manager can add users
        ProjectUser current = projectUserRepository.findByProject_ProjectIdAndUser_Id(project.getProjectId(),
                        userRepository.findByUsername(userDetails.getUsername()).orElseThrow().getId())
                .orElseThrow(() -> new RuntimeException("You are not a member of this project"));
        if (!current.getRole().name().equals("CREATOR") && !current.getRole().name().equals("MEMBER")) {
            throw new RuntimeException("You don't have permission to add users");
        }

        if (projectUserRepository.findByProject_ProjectIdAndUser_Id(project.getProjectId(), user.getId()).isPresent()) {
            throw new RuntimeException("User already in project");
        }

        ProjectUser projectUser = ProjectUser.builder()
                .project(project)
                .user(user)
                .role(Project.ProjectRole.MEMBER)
                .status(ProjectUser.RequestStatus.PENDING)
                .joinedAt(null)
                .build();
        return ProjectUserDTO.fromEntity(projectUserRepository.save(projectUser));
    }

    public void removeUserFromProject(Long projectId, Long userId, UserDetails userDetails) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProjectUser current = projectUserRepository.findByProject_ProjectIdAndUser_Id(projectId, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("You are not a member of this project"));
        if (!current.getRole().name().equals("OWNER") && !current.getRole().name().equals("MANAGER")) {
            throw new RuntimeException("You don't have permission to remove users");
        }

        if (!projectUserRepository.findByProject_ProjectIdAndUser_Id(projectId, userId).isPresent()) {
            throw new RuntimeException("User is not a member of this project");
        }
        int member = project.getNoOfEmployees();
        member--;
        project.setNoOfEmployees(member);
        projectRepository.save(project);

        projectUserRepository.deleteByProject_ProjectIdAndUser_Id(projectId, userId);
    }

    public ProjectUserDTO acceptInvitation(Long projectId, UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        ProjectUser projectUser = projectUserRepository.findByProject_ProjectIdAndUser_Id(projectId, user.getId())
                .orElseThrow(() -> new RuntimeException("Invitation not found"));

        if (projectUser.getStatus() != ProjectUser.RequestStatus.PENDING) {
            throw new RuntimeException("Invitation already processed");
        }
        Project project = projectRepository.findById(projectId).orElseThrow(()-> new RuntimeException("Project not found"));
        int member = project.getNoOfEmployees();
        member++;
        project.setNoOfEmployees(member);
        projectRepository.save(project);
        projectUser.setStatus(ProjectUser.RequestStatus.APPROVED);
        projectUser.setJoinedAt(LocalDateTime.now());
        return ProjectUserDTO.fromEntity(projectUserRepository.save(projectUser));
    }

    public ProjectUserDTO rejectInvitation(Long projectId, UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        ProjectUser projectUser = projectUserRepository.findByProject_ProjectIdAndUser_Id(projectId, user.getId())
                .orElseThrow(() -> new RuntimeException("Invitation not found"));

        if (projectUser.getStatus() != ProjectUser.RequestStatus.PENDING) {
            throw new RuntimeException("Invitation already processed");
        }

        projectUser.setStatus(ProjectUser.RequestStatus.REJECTED);
        return ProjectUserDTO.fromEntity(projectUserRepository.save(projectUser));
    }

}
