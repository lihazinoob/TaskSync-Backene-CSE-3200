package com.example.careerPilot.demo.service;

import com.example.careerPilot.demo.dto.UserProfileUpdateRequest;
import com.example.careerPilot.demo.entity.User;
import com.example.careerPilot.demo.repository.userRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final userRepository userRepository;

    public User getUserByUsername(String username) {
        log.debug("Fetching user with username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    @Transactional
    public User updateUserProfile(String username, UserProfileUpdateRequest updateRequest) {
        log.debug("Updating profile for user: {}", username);

        User user = getUserByUsername(username);

        // Update only the fields that are provided in the request
        if (updateRequest.getFirstName() != null) {
            user.setFirstName(updateRequest.getFirstName());
        }
        if (updateRequest.getLastName() != null) {
            user.setLastName(updateRequest.getLastName());
        }
        if (updateRequest.getEmail() != null) {
            user.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getPhoneNumber() != null) {
            user.setPhoneNumber(updateRequest.getPhoneNumber());
        }
        if (updateRequest.getCountry() != null) {
            user.setCountry(updateRequest.getCountry());
        }
        if (updateRequest.getProfileImage() != null) {
            user.setProfileImage(updateRequest.getProfileImage());
        }
        if (updateRequest.getExperienceYears() != null) {
            user.setExperienceYears(updateRequest.getExperienceYears());
        }
        if (updateRequest.getIndustry() != null) {
            user.setIndustry(updateRequest.getIndustry());
        }
        if (updateRequest.getAvailabilityStatus() != null) {
            user.setAvailabilityStatus(updateRequest.getAvailabilityStatus());
        }
        if (updateRequest.getPreferredWorkingHours() != null) {
            user.setPreferredWorkingHours(updateRequest.getPreferredWorkingHours());
        }
        if (updateRequest.getLanguage() != null) {
            user.setLanguage(updateRequest.getLanguage());
        }

        return userRepository.save(user);
    }
}