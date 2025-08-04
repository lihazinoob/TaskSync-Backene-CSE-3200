package com.example.careerPilot.demo.controller;


import com.example.careerPilot.demo.dto.ProjectDTO;
import com.example.careerPilot.demo.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "http://localhost:5173/") // Allows requests from the frontend app
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{companyId}")
    public ResponseEntity<List<ProjectDTO>> getAllproject(@RequestBody Long companyId) {
        List<ProjectDTO> projects = projectService.getAllProjects(companyId);
        return ResponseEntity.ok(projects);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<ProjectDTO> createProject(@Valid @RequestBody ProjectDTO projectDTO , @AuthenticationPrincipal UserDetails userDetails) {
        log.debug("Creating project: {}", projectDTO);
        ProjectDTO createdProject = projectService.createProject(projectDTO,userDetails);
        return ResponseEntity.ok(createdProject);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectDTO projectDTO, @AuthenticationPrincipal UserDetails userDetails) {
        ProjectDTO updatedProject = projectService.updateProject(id, projectDTO, userDetails);
        return ResponseEntity.ok(updatedProject);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        projectService.deleteProject(id, userDetails);
        return ResponseEntity.noContent().build();
    }

}
