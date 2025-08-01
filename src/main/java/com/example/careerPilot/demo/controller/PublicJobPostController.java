package com.example.careerPilot.demo.controller;

import com.example.careerPilot.demo.dto.JobPostDTO;
import com.example.careerPilot.demo.service.JobPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins ="http://localhost:5173/")
@RestController
@RequestMapping("/api/job-posts")
@RequiredArgsConstructor
public class PublicJobPostController {

    private final JobPostService jobPostService;

    @GetMapping
    public List<JobPostDTO> getAllOpenJobPosts() {
        return jobPostService.getAllPublicJobPosts();
    }
}