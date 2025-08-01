package com.example.careerPilot.demo.repository;

import com.example.careerPilot.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface userRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

}