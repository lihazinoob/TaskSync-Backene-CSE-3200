package com.example.careerPilot.demo.controller;

import com.example.careerPilot.demo.dto.UserDTO;
import com.example.careerPilot.demo.dto.UserProfileUpdateRequest;
import com.example.careerPilot.demo.entity.User;
import com.example.careerPilot.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@Slf4j
//@CrossOrigin(origins ="http://localhost:5173/")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Get current user profile
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getCurrentUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("GET /api/users/profile called for user: {}", userDetails.getUsername());
        User user = userService.getUserByUsername(userDetails.getUsername());
        UserDTO userDTO = UserDTO.fromEntity(user);
        return ResponseEntity.ok(userDTO);
    }

    // Update current user profile
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/profile")
    public ResponseEntity<UserDTO> updateCurrentUserProfile(
            @Valid @RequestBody UserProfileUpdateRequest updateRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        log.info("PUT /api/users/profile called for user: {}", userDetails.getUsername());
        User updatedUser = userService.updateUserProfile(userDetails.getUsername(), updateRequest);
        UserDTO updatedUserDTO = UserDTO.fromEntity(updatedUser);
        return ResponseEntity.ok(updatedUserDTO);
    }

    // Get user profile by username (useful for public profiles)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile/{username}")
    public ResponseEntity<UserDTO> getUserProfileByUsername(@PathVariable String username) {
        log.info("GET /api/users/profile/{} called", username);
        User user = userService.getUserByUsername(username);
        UserDTO userDTO = UserDTO.fromEntity(user);
        return ResponseEntity.ok(userDTO);
    }
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/profile/updateinfo/{username}")
    public ResponseEntity<UserDTO> updateUserInformation(
            @PathVariable String username,
            @Valid @RequestBody UserProfileUpdateRequest updateRequest
    ) {
        //log.info("PUT /api/users/profile/update/{} called", username);
        return ResponseEntity.ok(userService.updateUserInformation(username, updateRequest));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/profile/updatestatus/{username}")
    public ResponseEntity<UserDTO> updateUserStatus(
            @PathVariable String username,
            @Valid @RequestBody UserProfileUpdateRequest updateRequest
    ) {
        log.info("PUT /api/users/profile/update/{} called", username);
        return ResponseEntity.ok(userService.updateUserStatus(username, updateRequest));
    }

}