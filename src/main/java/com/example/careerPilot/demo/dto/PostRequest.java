package com.example.careerPilot.demo.dto;

import com.example.careerPilot.demo.entity.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostRequest {
    @NotBlank(message = "Content cannot be empty")
    private String content;

    @Size(max = 255, message = "Image URL must be less than 255 characters")
    private String image;

    private Post.Visibility visibility = Post.Visibility.PUBLIC;
}