package com.example.careerPilot.demo.repository;


import com.example.careerPilot.demo.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation,Long> {

    List<Evaluation> findByEvaluatedToId(Long id);
}
