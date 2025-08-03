package com.example.careerPilot.demo.repository;

import com.example.careerPilot.demo.entity.Company;
import com.example.careerPilot.demo.entity.Invitation;
import com.example.careerPilot.demo.entity.Invitation.InvitationStatus;
import com.example.careerPilot.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByInvitedUserAndStatus(User user, InvitationStatus status);
    boolean existsByCompanyAndInvitedUserAndStatus(Company company, User user, InvitationStatus status);

    List<Invitation> findByInvitedUser(User invitedUser);
    void deleteByCompanyId(Long id);
}
