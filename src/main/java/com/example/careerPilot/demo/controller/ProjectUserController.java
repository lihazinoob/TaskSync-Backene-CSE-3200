package com.example.careerPilot.demo.controller;


import com.example.careerPilot.demo.dto.ProjectUserDTO;
import com.example.careerPilot.demo.entity.ProjectUser;
import com.example.careerPilot.demo.entity.User;
import com.example.careerPilot.demo.repository.ProjectUserRepository;
import com.example.careerPilot.demo.service.ProjectUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.example.careerPilot.demo.repository.userRepository;
import java.util.List;

@Slf4j
@CrossOrigin(origins = "http://localhost:5173/") // Allows requests from the frontend app
@RestController
@RequestMapping("/api/projects/users")
@RequiredArgsConstructor
public class ProjectUserController {
    private final ProjectUserService projectUserService;
    private final userRepository userRepository;
    private final ProjectUserRepository projectUserRepository;
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    public ResponseEntity<ProjectUserDTO> addUserToProject(@Valid @RequestBody ProjectUserDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(projectUserService.addUserToProject(dto, userDetails));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeUserFromProject(@RequestParam Long projectId, @RequestParam Long userId, @AuthenticationPrincipal UserDetails userDetails) {
        projectUserService.removeUserFromProject(projectId, userId, userDetails);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/accept")
    public ResponseEntity<ProjectUserDTO> acceptInvitation(@RequestParam Long projectId, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(projectUserService.acceptInvitation(projectId, userDetails));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/pending_requests")
    public List<ProjectUserDTO> getPendingRequestsForUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return projectUserRepository.findAll().stream()
                .filter(pu -> pu.getUser().getId().equals(user.getId()) && pu.getStatus() == ProjectUser.RequestStatus.PENDING)
                .map(ProjectUserDTO::fromEntity)
                .toList();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/reject")
    public ResponseEntity<ProjectUserDTO> rejectInvitation(@RequestParam Long projectId, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(projectUserService.rejectInvitation(projectId, userDetails));
    }

}
