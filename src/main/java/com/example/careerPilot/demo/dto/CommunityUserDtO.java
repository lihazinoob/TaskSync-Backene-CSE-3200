package com.example.careerPilot.demo.dto;
import com.example.careerPilot.demo.entity.CommunityUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunityUserDtO {
    private Long id;
    private CommunityUser.role role;
    private CommunityUser.status status;
    private String CommunityName;
    private Long UserId;

    public static CommunityUserDtO fromEntity(CommunityUser communityUser) {
        return CommunityUserDtO.builder()
                .id(communityUser.getId())
                .role(communityUser.getRole())
                .status(communityUser.getStatus())
                .CommunityName(communityUser.getCommunity().getName())
                .UserId(communityUser.getUser().getId())
                .build();
    }
}
