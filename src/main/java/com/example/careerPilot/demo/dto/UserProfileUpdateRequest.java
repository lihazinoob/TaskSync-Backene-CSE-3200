package com.example.careerPilot.demo.dto;

import com.example.careerPilot.demo.entity.User;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserProfileUpdateRequest {
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Email(message = "Please provide a valid email address")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be 10-15 digits with an optional + prefix")
    private String phoneNumber;

    @Size(max = 60, message = "Country name cannot exceed 60 characters")
    private String country;

    @Size(max = 512, message = "Profile image URL cannot exceed 255 characters")
    @Pattern(regexp = "^(https?://.*|)$", message = "Profile image must be a valid URL or empty")
    private String profileImage;

    @Min(value = 0, message = "Experience years cannot be negative")
    @Max(value = 70, message = "Experience years cannot exceed 70")
    private Integer experienceYears;

    @Size(max = 100, message = "Industry name cannot exceed 100 characters")
    private String industry;

    private User.AvailabilityStatus availabilityStatus;

    @Pattern(regexp = "^([0-9]{1,2}(AM|PM|am|pm)-[0-9]{1,2}(AM|PM|am|pm)( [A-Za-z]{3,4})?|)$",
            message = "Working hours should be in format like '9AM-5PM EST'")
    private String preferredWorkingHours;

    @Size(min = 2, max = 10, message = "Language code should be between 2 and 10 characters")
    @Pattern(regexp = "^[a-zA-Z]{2,10}$", message = "Language code should contain only letters")
    private String language;
}