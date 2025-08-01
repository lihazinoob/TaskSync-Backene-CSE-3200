package com.example.careerPilot.demo.dto;

import com.example.careerPilot.demo.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class PostDTO {
    private Long postId;
    private String content;
    private String image;
    private Post.Visibility visibility;
    private int likesCount;
    private int sharesCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String username;
    public static PostDTO fromEntity(Post post) {

        return PostDTO.builder()
                .postId(post.getPostId())
                .content(post.getContent())
                .image(post.getImage())
                .visibility(post.getVisibility())
                .likesCount(post.getLikesCount())
                .sharesCount(post.getSharesCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .username(post.getUser() != null ? post.getUser().getUsername() : null)
                .build();
    }
}