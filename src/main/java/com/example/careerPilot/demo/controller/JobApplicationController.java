package com.example.careerPilot.demo.controller;

import com.example.careerPilot.demo.dto.JobApplicationDTO;
import com.example.careerPilot.demo.dto.JobApplicationRequest;
import com.example.careerPilot.demo.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService applicationService;

    @PostMapping("/{jobPostId}/apply")
    @ResponseStatus(HttpStatus.CREATED)
    public JobApplicationDTO applyToJob(
            @PathVariable Long jobPostId,
            @RequestBody JobApplicationRequest request,
            Authentication authentication) {
        return applicationService.applyToJob(jobPostId, request, authentication.getName());
    }

    @GetMapping("/{jobPostId}/applications")
    public List<JobApplicationDTO> getApplications(
            @PathVariable Long jobPostId,
            Authentication authentication) {
        return applicationService.getApplicationsForJob(jobPostId, authentication.getName());
    }

    @PostMapping("/applications/{applicationId}/process")
    public JobApplicationDTO processApplication(
            @RequestParam boolean accept,
            @PathVariable Long applicationId,
            @RequestParam Long companyId,
            Authentication authentication) {
        return applicationService.processApplication(
                companyId,
                applicationId,
                accept,
                authentication.getName());
    }
}