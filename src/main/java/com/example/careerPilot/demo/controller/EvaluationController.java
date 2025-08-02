package com.example.careerPilot.demo.controller;

import com.example.careerPilot.demo.dto.EvaluationDTO;
import com.example.careerPilot.demo.repository.userRepository;
import com.example.careerPilot.demo.service.EvaluationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "http://localhost:5173/") // Allows requests from the frontend app
@RestController
@RequestMapping("/api/evaluation")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/submit")
    public ResponseEntity<EvaluationDTO> submitEvaluation(@Valid @RequestBody EvaluationDTO evaluationDTO) {
        EvaluationDTO savedEvaluation = evaluationService.submitEvaluation(evaluationDTO);
        return ResponseEntity.ok(savedEvaluation);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<List<EvaluationDTO>> getEvaluationsByUserId(@PathVariable Long id) {
        log.debug("Fetching evaluations for user ID: {}", id);
        List<EvaluationDTO> evaluations = evaluationService.getEvaluationsByUserId(id);
        return ResponseEntity.ok(evaluations);
    }
}
