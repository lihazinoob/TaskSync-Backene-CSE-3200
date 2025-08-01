package com.example.careerPilot.demo.repository;

import com.example.careerPilot.demo.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByJobPostId(Long jobPostId);

    Optional<JobApplication> findByJobPostIdAndApplicantId(Long jobPostId, Long applicantId);

    boolean existsByJobPostIdAndApplicantId(Long jobPostId, Long applicantId);
}