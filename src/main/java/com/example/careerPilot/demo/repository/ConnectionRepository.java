package com.example.careerPilot.demo.repository;

import com.example.careerPilot.demo.entity.Connection;
import com.example.careerPilot.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {

     public Connection findByUser1AndUser2(User user1, User user2);
     public List<Connection> findByUser2(User user2);
}
