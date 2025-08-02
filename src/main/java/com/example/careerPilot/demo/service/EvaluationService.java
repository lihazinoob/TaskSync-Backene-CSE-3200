package com.example.careerPilot.demo.service;

import com.example.careerPilot.demo.dto.EvaluationDTO;
import com.example.careerPilot.demo.entity.Evaluation;
import com.example.careerPilot.demo.entity.User;
import com.example.careerPilot.demo.repository.EvaluationRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.careerPilot.demo.repository.userRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;
    private final userRepository userRepository;

    public EvaluationDTO submitEvaluation(@Valid EvaluationDTO evaluationDTO) {
        String evaluatedByUsername = evaluationDTO.getEvaluatedByUsername();
        String evaluatedToUsername = evaluationDTO.getEvaluatedToUsername();
        User evaluatedBy = userRepository.findByUsername(evaluatedByUsername).orElseThrow(()-> new RuntimeException("User not found with username: " + evaluatedToUsername));
        User evaluatedUser = userRepository.findByUsername(evaluatedToUsername).orElseThrow(() -> new RuntimeException("User not found with username: " + evaluatedByUsername));
        BigDecimal current_rating = evaluatedUser.getPerformanceRating();
        if(current_rating == null) {
            current_rating = BigDecimal.ZERO; // Initialize to zero if no previous rating exists
        }
        int totalEvaluations = evaluatedUser.getTotalReview();
        BigDecimal totalEvaluationsBD = BigDecimal.valueOf(totalEvaluations);

        // Calculate new rating as weighted average
        BigDecimal new_rating = (current_rating.multiply(totalEvaluationsBD)
                .add(evaluationDTO.getRating()))
                .divide(BigDecimal.valueOf(totalEvaluations + 1), BigDecimal.ROUND_HALF_UP);
        evaluatedUser.setPerformanceRating(new_rating);
        evaluatedUser.setTotalReview(totalEvaluations+1);
        evaluatedUser.setLastEvaluationDate(LocalDate.now());
        userRepository.save(evaluatedUser);
        Evaluation evaluation = Evaluation.builder()
                .evaluatedBy(evaluatedBy)
                .evaluatedTo(evaluatedUser)
                .rating(new_rating)
                .review(evaluationDTO.getReview())
                .build();
        return EvaluationDTO.fromEntity(evaluationRepository.save(evaluation));
    }

    public List<EvaluationDTO> getEvaluationsByUserId(Long id) {
        log.debug("Fetching evaluations for user ID: {}", id);
        return evaluationRepository.findByEvaluatedToId(id).stream()
                .map(EvaluationDTO::fromEntity)
                .toList();
    }
}
