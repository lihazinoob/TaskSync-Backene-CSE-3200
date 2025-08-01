package com.example.careerPilot.demo.service;

import com.example.careerPilot.demo.dto.CommunityDTO;
import com.example.careerPilot.demo.entity.Community;
import com.example.careerPilot.demo.entity.CommunityUser;
import com.example.careerPilot.demo.entity.User;
import com.example.careerPilot.demo.repository.CommunityRepository;
import com.example.careerPilot.demo.repository.CommunityUserRepository;
import com.example.careerPilot.demo.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommunityService {
    @Autowired
    private final CommunityRepository communityRepository;
    @Autowired
    private final userRepository userRepository;
    @Autowired
    private final CommunityUserRepository communityUserRepository;

    public CommunityService(CommunityRepository communityRepository, userRepository userRepository, CommunityUserRepository communityUserRepository) {
        this.communityRepository = communityRepository;
        this.userRepository = userRepository;
        this.communityUserRepository = communityUserRepository;
    }

    public Page<CommunityDTO> getCommunities(Pageable pageable) {
        return communityRepository.findAll(pageable).map(this::mapToDTO);
    }

    public Community createCommunity(@RequestBody CommunityDTO communityDTO, @RequestBody Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Community community = new Community();
        community.setName(communityDTO.getName());
        community.setDescription(communityDTO.getDescription());
        community.setCategory(communityDTO.getCategory());
        community.setCreatedBy(user);
        community.setMemberCount(1L);

        if (communityDTO.getVisibility() != null) {
            community.setVisibility(Community.Visibility.valueOf(communityDTO.getVisibility().toUpperCase()));
        }
        CommunityUser communityUser = new CommunityUser();
        communityUser.setCommunity(community);
        communityUser.setUser(user);
        communityUser.setRole(CommunityUser.role.ADMIN);

        Community community1 =  communityRepository.save(community);
        communityUserRepository.save(communityUser);
        return community1;

    }

    private CommunityDTO mapToDTO(Community community) {
        CommunityDTO dto = new CommunityDTO();
        dto.setId(community.getId());
        dto.setName(community.getName());
        dto.setDescription(community.getDescription());
        dto.setCategory(community.getCategory());
        dto.setVisibility(community.getVisibility().toString()); // Enum as string
        return dto;
    }


    public Page<CommunityDTO> getByCreator(Long id, Pageable pageable) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return communityRepository.findByCreatedBy(user,pageable).map(community -> mapToDTO(community));
    }

    public void deleteCommunity(Long id) {
        communityRepository.deleteById(id);
    }
}
