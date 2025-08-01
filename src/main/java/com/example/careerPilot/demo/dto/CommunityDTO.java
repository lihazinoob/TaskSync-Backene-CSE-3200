package com.example.careerPilot.demo.dto;

import com.example.careerPilot.demo.entity.Community;
import com.example.careerPilot.demo.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunityDTO {
    private Long id;
    private String name;
    private String description;
    private String category;
    private String visibility;
    public static CommunityDTO fromEntity(Community community) {

        return CommunityDTO.builder()
                .id(community.getId())
                .name(community.getName())
                .description(community.getDescription())
                .category(community.getCategory())
                .visibility(community.getVisibility().toString())
                .build();
    }
}