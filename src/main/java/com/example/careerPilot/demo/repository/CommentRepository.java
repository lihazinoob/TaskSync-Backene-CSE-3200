package com.example.careerPilot.demo.repository;

import com.example.careerPilot.demo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostPostId(Long postId);

    // find top-level comments (no parent)
    List<Comment> findByPostPostIdAndParentCommentIsNull(Long postId);

    // find child comments for a parent
    List<Comment> findByParentCommentId(Long parentId);
}