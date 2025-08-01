package com.example.careerPilot.demo.security;

import com.example.careerPilot.demo.entity.CompanyEmployee;
import com.example.careerPilot.demo.repository.CompanyEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component("companySecurity")
@RequiredArgsConstructor
public class CompanySecurity {

    private final CompanyEmployeeRepository companyEmployeeRepository;

    public boolean isCompanyAdmin(Long companyId, String username) {
        if (companyId == null || username == null || username.isEmpty()) {
            return false;
        }
        return companyEmployeeRepository.existsByCompanyIdAndUserUsernameAndRole(
                companyId, username, CompanyEmployee.Role.ADMIN);
    }
}