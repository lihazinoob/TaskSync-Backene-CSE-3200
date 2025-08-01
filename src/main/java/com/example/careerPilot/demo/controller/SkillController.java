package com.example.careerPilot.demo.controller;

import com.example.careerPilot.demo.dto.SkillDTO;
import com.example.careerPilot.demo.entity.Skill;
import com.example.careerPilot.demo.service.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    // Get all skills
    @GetMapping
    public ResponseEntity<List<SkillDTO>> getAllSkills() {
        log.info("GET /api/skills called");
        List<Skill> skills = skillService.getAllSkills();
        List<SkillDTO> skillDTOs = skills.stream()
                .map(SkillDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(skillDTOs);
    }

    // Get skill by ID
    @GetMapping("/{id}")
    public ResponseEntity<SkillDTO> getSkillById(@PathVariable Long id) {
        log.info("GET /api/skills/{} called", id);
        Skill skill = skillService.getSkillById(id);
        return ResponseEntity.ok(SkillDTO.fromEntity(skill));
    }

    // Create a new skill (admin only)
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SkillDTO> createSkill(@Valid @RequestBody SkillDTO skillDTO) {
        log.info("POST /api/skills called with skill name: {}", skillDTO.getSkillName());
        Skill skill = skillService.createSkill(skillDTO.getSkillName());
        return new ResponseEntity<>(SkillDTO.fromEntity(skill), HttpStatus.CREATED);
    }

    // Update a skill (admin only)
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SkillDTO> updateSkill(
            @PathVariable Long id,
            @Valid @RequestBody SkillDTO skillDTO) {
        log.info("PUT /api/skills/{} called with skill name: {}", id, skillDTO.getSkillName());
        Skill updatedSkill = skillService.updateSkill(id, skillDTO.getSkillName());
        return ResponseEntity.ok(SkillDTO.fromEntity(updatedSkill));
    }

    // Delete a skill (admin only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        log.info("DELETE /api/skills/{} called", id);
        skillService.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }

    // Search skills by name (contains)
    @GetMapping("/search")
    public ResponseEntity<List<SkillDTO>> searchSkills(@RequestParam String name) {
        log.info("GET /api/skills/search called with name: {}", name);
        List<Skill> skills = skillService.searchSkillsByName(name);
        List<SkillDTO> skillDTOs = skills.stream()
                .map(SkillDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(skillDTOs);
    }
}