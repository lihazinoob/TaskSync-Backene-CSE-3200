package com.example.careerPilot.demo.controller;

import com.example.careerPilot.demo.dto.CompanyDTO;
import com.example.careerPilot.demo.dto.CompanyRequest;
import com.example.careerPilot.demo.entity.Company;
import com.example.careerPilot.demo.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin(origins ="http://localhost:5173/")
@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAllCompany() {
        List<Company> companies= companyService.getAllCompanies();
        List<CompanyDTO> companyDTOS = companies.stream().map(CompanyDTO::fromEntity).collect(Collectors.toList());

        return ResponseEntity.ok(companyDTOS);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/creator/{creatorId}/allcompanies")
    public ResponseEntity<List<CompanyDTO>> getAllCompaniesByCreatorId(@PathVariable Long creatorId) {
        List<Company> companies = companyService.getAllCompaniesByCreatorId(creatorId);
        List<CompanyDTO> companyDTOS = companies.stream().map(CompanyDTO::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(companyDTOS);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(
            @Valid @RequestBody CompanyDTO request,
            @AuthenticationPrincipal UserDetails userDetails)
    {
        CompanyDTO company = companyService.createCompany(request, userDetails.getUsername());
        return new ResponseEntity<>(company, HttpStatus.CREATED);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")

    public ResponseEntity<?> updateCompany(
            @PathVariable Long id,
            @Valid @RequestBody CompanyDTO request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {

            return ResponseEntity.ok(companyService.updateCompany(id, request, userDetails.getUsername()));


        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());

        }

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteCompany(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        try {
            companyService.deleteCompany(id, userDetails.getUsername());
            return ResponseEntity.ok("Company deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompanyById(@PathVariable Long id) {
        CompanyDTO response = companyService.getCompanyById(id);
        return ResponseEntity.ok(response);
    }
}
