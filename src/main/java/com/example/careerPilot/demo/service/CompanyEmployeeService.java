package com.example.careerPilot.demo.service;

import com.example.careerPilot.demo.dto.CompanyEmployeeDTO;
import com.example.careerPilot.demo.entity.Company;
import com.example.careerPilot.demo.entity.CompanyEmployee;
import com.example.careerPilot.demo.entity.User;
import com.example.careerPilot.demo.exception.ResourceNotFoundException;
import com.example.careerPilot.demo.exception.UnauthorizedException;
import com.example.careerPilot.demo.repository.CompanyEmployeeRepository;
import com.example.careerPilot.demo.repository.CompanyRepository;
import com.example.careerPilot.demo.repository.userRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyEmployeeService {

    private final CompanyEmployeeRepository companyEmployeeRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public List<CompanyEmployeeDTO> getEmployeesByCompanyId(Long companyId) {
        // Verify company exists
        companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));

        return companyEmployeeRepository.findAllByCompanyId(companyId)
                .stream()
                .map(CompanyEmployeeDTO::fromEntity)
                .collect(Collectors.toList());
    }
}