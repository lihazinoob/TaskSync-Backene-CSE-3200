package com.example.careerPilot.demo.repository;

import com.example.careerPilot.demo.entity.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Long> {
    List<JobPost> findByCompanyId(Long companyId);
//    List<JobPost> findAllByStatus(JobPost.Status status);

    List<JobPost> findAllByFulfilledFalse();

    @Query("SELECT jp FROM JobPost jp LEFT JOIN FETCH jp.jobSkills WHERE jp.id = :id")
    Optional<JobPost> findByIdWithSkills(@Param("id") Long id);

    void deleteByCompanyId(Long id);
}