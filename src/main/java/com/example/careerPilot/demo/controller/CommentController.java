package com.example.careerPilot.demo.controller;

import com.example.careerPilot.demo.dto.CommentDTO;
import com.example.careerPilot.demo.dto.CommentRequest;
import com.example.careerPilot.demo.entity.Comment;
import com.example.careerPilot.demo.exception.CommentNotFoundException;
import com.example.careerPilot.demo.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all")
    public ResponseEntity<List<CommentDTO>> getCommentsByPostId(@PathVariable Long postId) {
        log.info("GET /api/posts/{}/comments/all called", postId);
        return ResponseEntity.ok(commentService.getAllCommentsByPostId(postId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<CommentDTO>> getFirstLayerCommentsByPostId(@PathVariable Long postId) {
        log.info("GET /api/posts/{}/comments called", postId);
        return ResponseEntity.ok(commentService.getFirstLayerCommentsByPostId(postId));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<CommentDTO> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest commentRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("POST /api/posts/{}/comments called", postId);
        CommentDTO savedComment = commentService.createComment(postId, commentRequest, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{commentId}/replies")
    public ResponseEntity<List<CommentDTO>> getRepliesForComment(
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        log.info("GET /api/posts/{}/comments/{}/replies called", postId, commentId);
        return ResponseEntity.ok(commentService.getRepliesForComment(commentId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDTO> getCommentById(
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        log.info("GET /api/posts/{}/comments/{} called", postId, commentId);
        return ResponseEntity.ok(commentService.getCommentById(commentId));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest commentRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("PUT /api/posts/{}/comments/{} called", postId, commentId);
        return ResponseEntity.ok(commentService.updateComment(commentId, commentRequest, userDetails.getUsername()));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("DELETE /api/posts/{}/comments/{} called", postId, commentId);
        commentService.deleteComment(commentId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}