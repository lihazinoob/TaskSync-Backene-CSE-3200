package com.example.careerPilot.demo.dto;

import com.example.careerPilot.demo.entity.CompanyEmployee;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompanyEmployeeDTO {
    private Long id;
    private Long userId;
    private String username;
    private String jobTitle;
    private CompanyEmployee.Role role;
    private CompanyEmployee.Status status;
    private LocalDateTime hiringDate;
    private LocalDateTime releasedAt;

    public static CompanyEmployeeDTO fromEntity(CompanyEmployee employee) {
        CompanyEmployeeDTO dto = new CompanyEmployeeDTO();
        dto.setId(employee.getId());
        dto.setUserId(employee.getUser().getId());
        dto.setUsername(employee.getUser().getUsername());
        dto.setJobTitle(employee.getJobTitle());
        dto.setRole(employee.getRole());
        dto.setStatus(employee.getStatus());
        dto.setHiringDate(employee.getHiringDate());
        dto.setReleasedAt(employee.getReleasedAt());
        return dto;
    }
}