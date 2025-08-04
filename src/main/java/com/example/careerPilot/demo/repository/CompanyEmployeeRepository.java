package com.example.careerPilot.demo.repository;

import com.example.careerPilot.demo.entity.Company;
import com.example.careerPilot.demo.entity.CompanyEmployee;
import com.example.careerPilot.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyEmployeeRepository extends JpaRepository<CompanyEmployee, Long> {
    boolean existsByUserAndCompany(User user, Company company);
    boolean existsByCompanyIdAndUserUsernameAndRole(Long companyId, String username, CompanyEmployee.Role role);

    @Query("SELECT ce FROM CompanyEmployee ce JOIN FETCH ce.user WHERE ce.company.id = :companyId")
    List<CompanyEmployee> findAllByCompanyId(@Param("companyId") Long companyId);
    int countByCompany(Company company);

    void deleteByCompanyId(Long id);
}