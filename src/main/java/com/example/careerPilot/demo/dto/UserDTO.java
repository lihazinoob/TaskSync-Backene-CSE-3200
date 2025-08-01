package com.example.careerPilot.demo.dto;

import com.example.careerPilot.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String country;
    private String profileImage;
    private Integer experienceYears;
    private String industry;
    private User.AvailabilityStatus availabilityStatus;
    private String preferredWorkingHours;
    private BigDecimal performanceRating;
    private LocalDate lastEvaluationDate;
    private Integer completedProjects;
    private Integer ongoingProjects;
    private User.AccountStatus accountStatus;
    private String language;

    public static UserDTO fromEntity(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .country(user.getCountry())
                .profileImage(user.getProfileImage())
                .experienceYears(user.getExperienceYears())
                .industry(user.getIndustry())
                .availabilityStatus(user.getAvailabilityStatus())
                .preferredWorkingHours(user.getPreferredWorkingHours())
                .performanceRating(user.getPerformanceRating())
                .lastEvaluationDate(user.getLastEvaluationDate())
                .completedProjects(user.getCompletedProjects())
                .ongoingProjects(user.getOngoingProjects())
                .accountStatus(user.getAccountStatus())
                .language(user.getLanguage())
                .build();
    }
}