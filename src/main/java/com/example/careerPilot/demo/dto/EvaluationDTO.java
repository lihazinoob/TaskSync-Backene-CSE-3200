package com.example.careerPilot.demo.dto;


import com.example.careerPilot.demo.entity.Evaluation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class EvaluationDTO {
    private Long EvaluationId;
    private String evaluatedByUsername;
    private String evaluatedToUsername;
    private String review;
    private BigDecimal rating;
    private LocalDateTime evaluationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public static EvaluationDTO fromEntity(Evaluation evaluation) {
        return EvaluationDTO.builder()
                .EvaluationId(evaluation.getEvaluationId())
                .evaluatedByUsername(evaluation.getEvaluatedBy() != null ? evaluation.getEvaluatedBy().getUsername() : null)
                .evaluatedToUsername(evaluation.getEvaluatedTo() != null ? evaluation.getEvaluatedTo().getUsername() : null)
                .review(evaluation.getReview())
                .rating(evaluation.getRating())
                .evaluationDate(evaluation.getEvaluationDate())
                .createdAt(evaluation.getCreatedAt())
                .updatedAt(evaluation.getUpdatedAt())
                .build();
    }
}
