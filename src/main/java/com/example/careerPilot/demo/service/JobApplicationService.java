package com.example.careerPilot.demo.service;

import com.example.careerPilot.demo.dto.JobApplicationDTO;
import com.example.careerPilot.demo.dto.JobApplicationRequest;
import com.example.careerPilot.demo.entity.*;
import com.example.careerPilot.demo.exception.*;
import com.example.careerPilot.demo.repository.*;
import com.example.careerPilot.demo.security.CompanySecurity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final JobPostRepository jobPostRepository;
    private final userRepository userRepository;
    private final CompanyEmployeeRepository companyEmployeeRepository;
    private final CompanySecurity companySecurity;

    @Transactional
    public JobApplicationDTO applyToJob(Long jobPostId, JobApplicationRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new ResourceNotFoundException("Job post not found"));

        if (applicationRepository.existsByJobPostIdAndApplicantId(jobPostId, user.getId())) {
            throw new ConflictException("You have already applied to this job");
        }


        if (jobPost.isFulfilled()) {
            throw new BadRequestException("This job post is already fulfilled");
        }

        JobApplication application = JobApplication.builder()
                .jobPost(jobPost)
                .applicant(user)
                .status(JobApplication.Status.PENDING)
                .appliedAt(LocalDateTime.now())
                .build();

        return JobApplicationDTO.fromEntity(applicationRepository.save(application));
    }

    @Transactional
    public JobApplicationDTO processApplication(
            Long companyId,
            Long applicationId,
            boolean accept,
            String username) {

        if (!companySecurity.isCompanyAdmin(companyId, username)) {
            throw new UnauthorizedException("Only company admins can process applications");
        }

        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (!application.getJobPost().getCompany().getId().equals(companyId)) {
            throw new UnauthorizedException("Application doesn't belong to your company");
        }

        if (application.getStatus() != JobApplication.Status.PENDING) {
            throw new BadRequestException("Application has already been processed");
        }

        if (accept) {
            application.setStatus(JobApplication.Status.ACCEPTED);
            application.getJobPost().setFulfilled(true);


            CompanyEmployee employee = CompanyEmployee.builder()
                    .user(application.getApplicant())
                    .company(application.getJobPost().getCompany())
                    .role(CompanyEmployee.Role.EMPLOYEE)
                    .hiringDate(LocalDateTime.now())
                    .status(CompanyEmployee.Status.ACTIVE)
                    .jobTitle(application.getJobPost().getJobTitle())
                    .build();

            companyEmployeeRepository.save(employee);
        } else {
            application.setStatus(JobApplication.Status.REJECTED);
        }

        application.setProcessedAt(LocalDateTime.now());
        return JobApplicationDTO.fromEntity(applicationRepository.save(application));
    }

    public List<JobApplicationDTO> getApplicationsForJob(Long jobPostId, String username) {
        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new ResourceNotFoundException("Job post not found"));

        if (!companySecurity.isCompanyAdmin(jobPost.getCompany().getId(), username)) {
            throw new UnauthorizedException("Only company admins can view applications");
        }

        return applicationRepository.findByJobPostId(jobPostId).stream()
                .map(JobApplicationDTO::fromEntity)
                .toList();
    }
}