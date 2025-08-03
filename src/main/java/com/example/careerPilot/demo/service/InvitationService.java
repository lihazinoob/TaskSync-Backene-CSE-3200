
package com.example.careerPilot.demo.service;

import com.example.careerPilot.demo.dto.InvitationDTO;
import com.example.careerPilot.demo.dto.InvitationRequest;
import com.example.careerPilot.demo.entity.*;
import com.example.careerPilot.demo.exception.InvitationNotFoundException;
import com.example.careerPilot.demo.repository.CompanyEmployeeRepository;
import com.example.careerPilot.demo.repository.CompanyRepository;
import com.example.careerPilot.demo.repository.InvitationRepository;
import com.example.careerPilot.demo.repository.userRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final userRepository userRepository;
    private final CompanyRepository companyRepository;
    private final CompanyEmployeeRepository companyEmployeeRepository;

    @Transactional
    public InvitationDTO createInvitation( InvitationRequest request, String inviterUsername) {
        User inviter = userRepository.findByUsername(inviterUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
        User invitedUser = userRepository.findById(request.getInvitedUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invited user not found"));

        if(company.getCreatedBy()==null || !company.getCreatedBy().getUsername().equals(inviterUsername))
            throw new RuntimeException("You don't have permission to send invitation");


        if (invitationRepository.existsByCompanyAndInvitedUserAndStatus(
                company, invitedUser, Invitation.InvitationStatus.PENDING)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pending invitation already exists");
        }

        if (companyEmployeeRepository.existsByUserAndCompany(invitedUser, company)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already belongs to this company");
        }



        Invitation invitation = new Invitation();
        invitation.setCompany(company);
        invitation.setInvitedUser(invitedUser);
        invitation.setInviter(inviter);
        invitation.setRole(request.getRole());
        invitation.setStatus(Invitation.InvitationStatus.PENDING);
        invitation.setCreatedAt(LocalDateTime.now());

        return InvitationDTO.fromEntity(invitationRepository.save(invitation));
    }

    @Transactional
    public InvitationDTO acceptInvitation(Long invitationId, String username) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow();

        if (!invitation.getInvitedUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only accept your own invitations");
        }

        if (invitation.getStatus() != Invitation.InvitationStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invitation already processed");
        }

        CompanyEmployee employee = new CompanyEmployee();
        employee.setUser(invitation.getInvitedUser());
        employee.setCompany(invitation.getCompany());
        employee.setRole(invitation.getRole());
        employee.setHiringDate(LocalDateTime.now());
        employee.setStatus(CompanyEmployee.Status.ACTIVE);
        companyEmployeeRepository.save(employee);

        invitation.setStatus(Invitation.InvitationStatus.ACCEPTED);
        return InvitationDTO.fromEntity(invitationRepository.save(invitation));
    }

    public InvitationDTO rejectInvitation(Long invitationId, String username) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow();

        if (!invitation.getInvitedUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only reject your own invitations");
        }

        invitation.setStatus(Invitation.InvitationStatus.REJECTED);
        return InvitationDTO.fromEntity(invitationRepository.save(invitation));
    }
}