package com.example.careerPilot.demo.controller;

import com.example.careerPilot.demo.Reponse.ApiResponse;
import com.example.careerPilot.demo.dto.CommunityDTO;
import com.example.careerPilot.demo.entity.Community;
import com.example.careerPilot.demo.service.CommunityService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor

public class CommunityController {
    private final CommunityService communityService;
    // /api/community?page=0&size=10&sort=name,asc
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<?> getAllCommunities(Pageable pageable) {
        try {
            Page<CommunityDTO> communityDTOS = communityService.getCommunities(pageable);
            return ResponseEntity.status(HttpStatus.OK).body(communityDTOS);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while fetching communities: " + e.getMessage());
        }
    }

    // api/community?userId=x
    @PreAuthorize("isAuthenticated()")
    @PostMapping()
    @JsonIgnore
    public ResponseEntity<?> createCommunity(
            @RequestBody CommunityDTO communityDTO,
            @RequestParam Long userId) {
        try {
            Community community = communityService.createCommunity(communityDTO, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(CommunityDTO.fromEntity(community));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid input: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }
    //api/community/{id}?page=0&size=1
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<?> getByCreator(
            @PathVariable Long id,
            Pageable pageable) {
        try {
            Page<CommunityDTO> communityDTOS = communityService.getByCreator(id, pageable);
            return ResponseEntity.status(HttpStatus.OK).body(communityDTOS);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid input: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    // api/community/{id}
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommunity(@PathVariable Long id) {
        try {
            communityService.deleteCommunity(id);
            ApiResponse response = new ApiResponse("Community deleted successfully", Map.of("id", id));
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Invalid input: " + e.getMessage(), null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("An unexpected error occurred: " + e.getMessage(), null));
        }
    }

}
