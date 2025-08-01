package com.example.careerPilot.demo.repository;

import com.example.careerPilot.demo.entity.Community;
import com.example.careerPilot.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    Page<Community> findAll(Pageable pageable);
    Page<Community> findByCreatedBy(User user, Pageable pageable);

}
