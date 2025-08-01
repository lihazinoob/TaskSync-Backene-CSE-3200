package com.example.careerPilot.demo.repository;

import com.example.careerPilot.demo.dto.PostDTO;
import com.example.careerPilot.demo.entity.Community;
import com.example.careerPilot.demo.entity.Post;
import com.example.careerPilot.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long id);
    Page<Post> findPostByCommunity(Community community, Pageable pageable);
    Page<Post> findPostByUser(User user, Pageable pageable);
}
