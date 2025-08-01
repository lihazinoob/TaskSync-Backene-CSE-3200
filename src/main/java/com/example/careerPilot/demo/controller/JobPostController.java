package com.example.careerPilot.demo.controller;

import com.example.careerPilot.demo.dto.*;
import com.example.careerPilot.demo.service.JobPostService;
import org.springframework.web.bind.annotation.CrossOrigin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins ="http://localhost:5173/")
@RestController
@RequestMapping("/api/companies/{companyId}/job-posts")
@RequiredArgsConstructor
public class JobPostController {

    private final JobPostService jobPostService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobPostDTO createJobPost(
            @PathVariable Long companyId,
            @RequestBody @Valid JobPostRequest request,
            Authentication authentication) {
        return jobPostService.createJobPost(companyId, request, authentication.getName());
    }

    @PutMapping("/{postId}")
    public JobPostDTO updateJobPost(
            @PathVariable Long companyId,
            @PathVariable Long postId,
            @RequestBody @Valid JobPostRequest request,
            Authentication authentication) {
        return jobPostService.updateJobPost(companyId, postId, request, authentication.getName());
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteJobPost(
            @PathVariable Long companyId,
            @PathVariable Long postId,
            Authentication authentication) {
        jobPostService.deleteJobPost(companyId, postId, authentication.getName());
    }

    @GetMapping
    public List<JobPostDTO> getCompanyJobPosts(@PathVariable Long companyId) {
        return jobPostService.getCompanyJobPosts(companyId);
    }
}