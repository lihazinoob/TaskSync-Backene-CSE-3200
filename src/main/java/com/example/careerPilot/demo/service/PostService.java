package com.example.careerPilot.demo.service;

import com.example.careerPilot.demo.dto.PostDTO;
import com.example.careerPilot.demo.dto.PostRequest;
import com.example.careerPilot.demo.entity.Community;
import com.example.careerPilot.demo.entity.Post;
import com.example.careerPilot.demo.entity.User;
import com.example.careerPilot.demo.exception.PostNotFoundException;
import com.example.careerPilot.demo.repository.CommunityRepository;
import com.example.careerPilot.demo.repository.PostRepository;
import com.example.careerPilot.demo.repository.userRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j  // Enables logging
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final userRepository userRepository;
    private final CommunityRepository communityRepository;


    public List<PostDTO> getAllPosts() {
        log.debug("Fetching all posts from the database");
        return postRepository.findAll().stream()
                .map(PostDTO::fromEntity) // Use PostDTO.fromEntity
                .collect(Collectors.toList());
    }

    public PostDTO getPostById(Long id) throws PostNotFoundException {
        log.debug("Fetching post with ID: {}", id);
        return PostDTO.fromEntity(postRepository.findById(id)
                // Use PostDTO.fromEntity
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + id)));
    }




    public PostDTO createPost(PostRequest postRequest, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        Post post = Post.builder()
                .content(postRequest.getContent())
                .image(postRequest.getImage())
                .visibility(postRequest.getVisibility())
                .user(user)
                .approved(true)
                .build();
        return PostDTO.fromEntity(postRepository.save(post));
    }


    public void deletePost(Long id, String username) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        if (!post.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You don't have permission to delete this post");
        }
        postRepository.delete(post);
    }

    public PostDTO updatePost(Long id, PostRequest postRequest, String username) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        if (!existingPost.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You don't have permission to update this post");
        }
        existingPost.setContent(postRequest.getContent());
        existingPost.setImage(postRequest.getImage());
        existingPost.setVisibility(postRequest.getVisibility());
        return PostDTO.fromEntity(postRepository.save(existingPost));
    }

    public PostDTO createPostByCommunity(PostRequest postRequest, Long communityId, org.springframework.security.core.userdetails.UserDetails userDetails) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found with id: " + communityId));
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found with username: " + userDetails.getUsername()));
        Post post = Post.builder()
                .content(postRequest.getContent())
                .image(postRequest.getImage())
                .visibility(postRequest.getVisibility())
                .user(user)
                .community(community)
                .approved(true)
                .build();
        return PostDTO.fromEntity(postRepository.save(post));
    }

    public Page<PostDTO> getPostByCommunityId(Long communityId, Pageable pageable) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found with id: " + communityId));
        return postRepository.findPostByCommunity(community, pageable)
                .map(PostDTO::fromEntity);
    }


    public Page<PostDTO> getPostByUserId(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return postRepository.findPostByUser(user, pageable)
                .map(PostDTO::fromEntity);
    }
}


