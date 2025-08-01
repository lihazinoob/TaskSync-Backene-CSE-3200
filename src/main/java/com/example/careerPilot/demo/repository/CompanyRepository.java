package com.example.careerPilot.demo.repository;

import com.example.careerPilot.demo.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Override
    Optional<Company> findById(Long id);

    List<Company> findByCreatedBy_Id(Long creatorId);
}
