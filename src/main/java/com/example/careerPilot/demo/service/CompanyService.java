    package com.example.careerPilot.demo.service;

    import com.example.careerPilot.demo.dto.CompanyDTO;
    import com.example.careerPilot.demo.dto.CompanyRequest;
    import com.example.careerPilot.demo.entity.*;
    import com.example.careerPilot.demo.exception.CompanyNotFoundException;
    import com.example.careerPilot.demo.repository.*;
    import jakarta.transaction.Transactional;
    import lombok.RequiredArgsConstructor;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.stereotype.Service;

    import java.time.LocalDateTime;
    import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class CompanyService {

        private final CompanyRepository companyRepository;
        private final JobPostRepository jobPostRepository;
        private final userRepository userRepository;
        private final InvitationRepository invitationRepository;
        private final CompanyEmployeeRepository companyEmployeeRepository;

        public List<Company> getAllCompanies(){
            return companyRepository.findAll();

        }

        @Transactional
        public CompanyDTO createCompany(CompanyDTO request, String username) {
            User creator = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Company company = new Company();
            company.setCompanyName(request.getCompanyName());
            company.setDescriptions(request.getDescriptions());
            company.setIndustry(request.getIndustry());
            company.setLocation(request.getLocation());
            company.setContactEmail(request.getContactEmail());
            company.setCreatedBy(creator);
            company.setCompanyImageUrl(request.getCompanyImageURL().getUrl());
            company.setCompanyImagePublicId(request.getCompanyImageURL().getPublicId());
            company = companyRepository.save(company);

            // Add creator as admin
            CompanyEmployee adminEmployee = new CompanyEmployee();
            adminEmployee.setUser(creator);
            adminEmployee.setCompany(company);
            adminEmployee.setRole(CompanyEmployee.Role.ADMIN);
            adminEmployee.setHiringDate(LocalDateTime.now());
            adminEmployee.setStatus(CompanyEmployee.Status.ACTIVE);
            adminEmployee.setJobTitle("Founder");
            companyEmployeeRepository.save(adminEmployee);

            return CompanyDTO.fromEntity(company);
        }

        public CompanyDTO updateCompany(Long id, CompanyDTO request, String username) {
            Company company = companyRepository.findById(id)
                    .orElseThrow(()-> new CompanyNotFoundException("Company not found with id: " + id));

            if(company.getCreatedBy()==null || !company.getCreatedBy().getUsername().equals(username))
                throw new RuntimeException("You don't have permission to modify this company");

            company.setCompanyName(request.getCompanyName());
            company.setDescriptions(request.getDescriptions());
            company.setIndustry(request.getIndustry());
            company.setLocation(request.getLocation());
            company.setContactEmail(request.getContactEmail());
            company.setCompanyImageUrl(request.getCompanyImageURL().getUrl());
            company.setCompanyImagePublicId(request.getCompanyImageURL().getPublicId());

            return CompanyDTO.fromEntity(companyRepository.save(company));
        }

        @Transactional
        public void deleteCompany(Long id, String username) {
            Company company = companyRepository.findById(id)
                            .orElseThrow(()-> new RuntimeException("Company  not available"));

            if(company.getCreatedBy()==null || !company.getCreatedBy().getUsername().equals(username)){
                throw new RuntimeException("You dont have permission to delete this company");
            }

            invitationRepository.deleteByCompanyId(id);
            companyEmployeeRepository.deleteByCompanyId(id);
            jobPostRepository.deleteByCompanyId(id);
            companyRepository.deleteById(id);
        }

        public CompanyDTO getCompanyById(Long id) {
            return companyRepository.findById(id)
                    .map(CompanyDTO::fromEntity)
                    .orElseThrow();
        }

        public List<Company> getAllCompaniesByCreatorId(Long creatorId) {
            return companyRepository.findByCreatedBy_Id(creatorId);
        }
    }