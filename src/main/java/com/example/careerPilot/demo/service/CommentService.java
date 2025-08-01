package com.example.careerPilot.demo.service;

import com.example.careerPilot.demo.dto.CommentDTO;
import com.example.careerPilot.demo.dto.CommentRequest;
import com.example.careerPilot.demo.entity.Comment;
import com.example.careerPilot.demo.entity.Post;
import com.example.careerPilot.demo.entity.User;
import com.example.careerPilot.demo.exception.AccessDeniedException;
import com.example.careerPilot.demo.exception.CommentNotFoundException;
import com.example.careerPilot.demo.exception.ResourceNotFoundException;
import com.example.careerPilot.demo.repository.CommentRepository;
import com.example.careerPilot.demo.repository.PostRepository;
import com.example.careerPilot.demo.repository.userRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final userRepository userRepository;

    public List<CommentDTO> getAllCommentsByPostId(Long postId) {
        log.debug("Fetching all comments for post with ID: {}", postId);
        List<Comment> comments = commentRepository.findByPostPostId(postId);
        return comments.stream()
                .map(CommentDTO::fromComment)
                .collect(Collectors.toList());
    }

    public List<CommentDTO> getFirstLayerCommentsByPostId(Long postId) {
        log.debug("Fetching first layer comments for post with ID: {}", postId);
        List<Comment> comments = commentRepository.findByPostPostIdAndParentCommentIsNull(postId);
        return comments.stream()
                .map(CommentDTO::fromComment)
                .collect(Collectors.toList());
    }

    public CommentDTO createComment(Long postId, CommentRequest commentRequest, String username) {
        log.debug("Creating comment for post ID: {} by user: {}", postId, username);

        Post post = findPostById(postId);
        User user = findUserByUsername(username);

        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setPost(post);
        comment.setUser(user);

        // Handle reply if parentId is provided
        if (commentRequest.getParentId() != null) {
            Comment parentComment = findCommentById(commentRequest.getParentId());
            comment.setParentComment(parentComment);
        }

        Comment savedComment = commentRepository.save(comment);
        return CommentDTO.fromComment(savedComment);
    }

    public CommentDTO getCommentById(Long commentId) {
        log.debug("Fetching comment with ID: {}", commentId);
        Comment comment = findCommentById(commentId);
        return CommentDTO.fromComment(comment);
    }

    public CommentDTO updateComment(Long commentId, CommentRequest commentRequest, String username) {
        log.debug("Updating comment ID: {} by user: {}", commentId, username);

        Comment existingComment = findCommentById(commentId);

        validateCommentOwnership(existingComment, username);

        // Update only the content
        existingComment.setContent(commentRequest.getContent());

        Comment updatedComment = commentRepository.save(existingComment);
        return CommentDTO.fromComment(updatedComment);
    }

    public void deleteComment(Long commentId, String username) {
        log.debug("Attempting to delete comment ID: {} by user: {}", commentId, username);

        Comment comment = findCommentById(commentId);
        validateCommentOwnership(comment, username);

        commentRepository.delete(comment);
        log.info("Comment ID: {} successfully deleted by user: {}", commentId, username);
    }

    public List<CommentDTO> getRepliesForComment(Long commentId) {
        log.debug("Fetching replies for comment with ID: {}", commentId);
        List<Comment> replies = commentRepository.findByParentCommentId(commentId);
        return replies.stream()
                .map(CommentDTO::fromComment)
                .collect(Collectors.toList());
    }

    // Helper methods for common operations and error handling
    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
    }

    private void validateCommentOwnership(Comment comment, String username) {
        if (comment.getUser() == null || !comment.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You don't have permission to modify this comment");
        }
    }
}