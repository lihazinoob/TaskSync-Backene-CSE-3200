package com.example.careerPilot.demo.repository;

import com.example.careerPilot.demo.dto.CommunityUserDtO;
import com.example.careerPilot.demo.entity.Community;
import com.example.careerPilot.demo.entity.CommunityUser;
import com.example.careerPilot.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityUserRepository extends JpaRepository<CommunityUser, Long> {
    CommunityUser findByUserAndCommunity(User user , Community community);

    List<CommunityUser> findByUser(User user );

    Page<CommunityUser> findAllByCommunity(Community community, Pageable pageable);
}
