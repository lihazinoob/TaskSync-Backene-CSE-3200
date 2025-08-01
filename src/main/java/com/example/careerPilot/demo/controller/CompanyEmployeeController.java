package com.example.careerPilot.demo.controller;

import com.example.careerPilot.demo.dto.CompanyEmployeeDTO;
import com.example.careerPilot.demo.service.CompanyEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/companies/{companyId}/employees")
@RequiredArgsConstructor
public class CompanyEmployeeController {

    private final CompanyEmployeeService companyEmployeeService;

    @GetMapping
    public List<CompanyEmployeeDTO> getAllEmployeesByCompany(@PathVariable Long companyId) {
        return companyEmployeeService.getEmployeesByCompanyId(companyId);
    }
}