package com.example.careerPilot.demo.controller;

import com.example.careerPilot.demo.dto.PostDTO;
import com.example.careerPilot.demo.dto.PostRequest;
import com.example.careerPilot.demo.entity.Post;
import com.example.careerPilot.demo.exception.PostNotFoundException;
import com.example.careerPilot.demo.service.PostService;
import com.example.careerPilot.demo.repository.userRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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

@Slf4j  // Enables logging
@CrossOrigin(origins = "http://localhost:5173/") // Allows requests from the frontend app
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final userRepository userRepository;


    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        log.debug("Fetching all posts");
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) throws PostNotFoundException {
        log.debug("Fetching post with ID: {}", id);
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPostByUserId(@PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(postService.getPostByUserId(userId, pageable));
    }




    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostRequest postRequest,
                                        @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {
        return ResponseEntity.ok(postService.createPost(postRequest, userDetails.getUsername()));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id,
                                        @Valid @RequestBody PostRequest postRequest,
                                        @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {
        return ResponseEntity.ok(postService.updatePost(id, postRequest, userDetails.getUsername()));
    }


    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id,
                                        @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {
        postService.deletePost(id, userDetails.getUsername());
        return ResponseEntity.ok("Post deleted successfully");
    }

    //community post create
    // api/posts/communityId
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/communityPost/{communityId}")
    public ResponseEntity<?> createPostByCommunity(@Valid @RequestBody PostRequest postRequest,
                                                   @PathVariable Long communityId,
                                                   @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {
        return ResponseEntity.ok(postService.createPostByCommunity(postRequest, communityId, userDetails));
    }


    //get post by community
    // api/posts/communityId?page=0&size=10
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/communityPost/{communityId}")
    public ResponseEntity<?> getPostByCommunity(@PathVariable Long communityId, Pageable pageable) {
        return ResponseEntity.ok(postService.getPostByCommunityId(communityId, pageable));
    }



}
