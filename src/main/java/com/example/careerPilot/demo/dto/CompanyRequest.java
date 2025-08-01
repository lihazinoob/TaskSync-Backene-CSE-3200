package com.example.careerPilot.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CompanyRequest {
    @NotBlank(message = "Company name is required")
    @Size(max = 100)
    private String companyName;

    @Size(max = 1000)
    private String descriptions;

    @Size(max = 50)
    private String industry;

    @Size(max = 100)
    private String location;

    @Size(max = 255)
    private String contactEmail;
}