package com.example.careerPilot.demo.dto;

import com.example.careerPilot.demo.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long postId;
    private Long userId;
    private String username;
    private Long parentId;
    private List<CommentDTO> replies;

    public static CommentDTO fromComment(Comment comment) {

        List<CommentDTO> childDTOs = comment.getChildComments().stream()
                .map(CommentDTO::fromComment)
                .collect(Collectors.toList());

        return CommentDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .postId(comment.getPost() != null ? comment.getPost().getPostId() : null)
                .userId(comment.getUser() != null ? comment.getUser().getId() : null)
                .username(comment.getUser() != null ? comment.getUser().getUsername() : null)
                .parentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                .replies(childDTOs)
                .build();
    }
}