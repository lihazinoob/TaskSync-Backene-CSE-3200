package com.example.careerPilot.demo.controller;

import com.example.careerPilot.demo.dto.UpdateUserSkillRequest;
import com.example.careerPilot.demo.dto.UserSkillDTO;
import com.example.careerPilot.demo.dto.UserSkillRequest;
import com.example.careerPilot.demo.entity.UserSkill;
import com.example.careerPilot.demo.service.UserSkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin(origins ="http://localhost:5173/")
@RestController
@RequestMapping("/api/user-skills")
@RequiredArgsConstructor
public class UserSkillController {

    private final UserSkillService userSkillService;

    // Get current user's skills
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<UserSkillDTO>> getCurrentUserSkills(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("GET /api/user-skills called for user: {}", userDetails.getUsername());
        List<UserSkill> userSkills = userSkillService.getUserSkillsByUsername(userDetails.getUsername());
        List<UserSkillDTO> userSkillDTOs = userSkills.stream()
                .map(UserSkillDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userSkillDTOs);
    }

    // Add a skill to current user
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<UserSkillDTO> addSkillToCurrentUser(
            @Valid @RequestBody UserSkillRequest userSkillRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("POST /api/user-skills called for user: {} with skill ID: {}",
                userDetails.getUsername(), userSkillRequest.getSkillId());
        UserSkill userSkill = userSkillService.addSkillToUser(
                userDetails.getUsername(),
                userSkillRequest.getSkillId(),
                userSkillRequest.getProficiencyLevel());
        return new ResponseEntity<>(UserSkillDTO.fromEntity(userSkill), HttpStatus.CREATED);
    }

    // Update a user skill
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{userSkillId}")
    public ResponseEntity<UserSkillDTO> updateUserSkill(
            @PathVariable Long userSkillId,
            @Valid @RequestBody UpdateUserSkillRequest updateRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("PUT /api/user-skills/{} called for user: {}", userSkillId, userDetails.getUsername());
        UserSkill updatedUserSkill = userSkillService.updateUserSkill(
                userDetails.getUsername(),
                userSkillId,
                updateRequest.getProficiencyLevel());
        return ResponseEntity.ok(UserSkillDTO.fromEntity(updatedUserSkill));
    }

    // Delete a user skill
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{userSkillId}")
    public ResponseEntity<Void> deleteUserSkill(
            @PathVariable Long userSkillId,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("DELETE /api/user-skills/{} called for user: {}", userSkillId, userDetails.getUsername());
        userSkillService.deleteUserSkill(userDetails.getUsername(), userSkillId);
        return ResponseEntity.noContent().build();
    }

    // Get user skills by username (for public profiles)
    @GetMapping("/user/{username}")
    public ResponseEntity<List<UserSkillDTO>> getUserSkillsByUsername(@PathVariable String username) {
        log.info("GET /api/user-skills/user/{} called", username);
        List<UserSkill> userSkills = userSkillService.getUserSkillsByUsername(username);
        List<UserSkillDTO> userSkillDTOs = userSkills.stream()
                .map(UserSkillDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userSkillDTOs);
    }
}