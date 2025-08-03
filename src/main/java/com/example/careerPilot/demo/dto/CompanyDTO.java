package com.example.careerPilot.demo.dto;
import com.example.careerPilot.demo.entity.Company;
import com.example.careerPilot.demo.entity.CompanyEmployee;
import lombok.Data;

import java.time.LocalDateTime;



@Data
public class CompanyDTO {
    private Long id;
    private String companyName;
    private String descriptions;
    private String industry;
    private String location;
    private String contactEmail;
    private Integer noOfEmployee;
    private String createdByUsername;
    private LocalDateTime createdAt;
    private CompanyImageDTO companyImageURL;

    public static CompanyDTO fromEntity(Company company) {
        CompanyDTO dto = new CompanyDTO();
        dto.setId(company.getId());
        dto.setCompanyName(company.getCompanyName());
        dto.setDescriptions(company.getDescriptions());
        dto.setIndustry(company.getIndustry());
        dto.setLocation(company.getLocation());
        dto.setContactEmail(company.getContactEmail());
        dto.setNoOfEmployee(company.getNoOfEmployee());
        dto.setCreatedByUsername(company.getCreatedBy().getUsername());
        dto.setCreatedAt(company.getCreatedAt());
        dto.setCompanyImageURL(new CompanyImageDTO(company.getCompanyImageUrl(), company.getCompanyImagePublicId()));
        return dto;
    }
}