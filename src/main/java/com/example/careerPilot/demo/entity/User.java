package com.example.careerPilot.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "User")


public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "User name is required")
    @Column(name = "user_name", unique = true, nullable = false)
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Email(message = "Input valid email")
    @NotEmpty(message = "Email is required")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Size(min = 8, message = "8 character long")
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "country")
    private String country;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "experience_years", columnDefinition = "int default 0")
    private int experienceYears;

    @Column(name = "industry")
    private String industry;

    @Enumerated(EnumType.STRING)
    @Column(name = "availability_status", columnDefinition = "enum('available', 'busy', 'on_leave') default 'available'")
    private AvailabilityStatus availabilityStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "enum('USER','ADMIN') default 'USER'")
    private ROLE role;

    @Column(name = "preferred_working_hours")
    private String preferredWorkingHours;

    @Column(name = "performance_rating", precision = 3, scale = 2, columnDefinition = "decimal(3,2) default NULL")
    private BigDecimal performanceRating;

    @Column(name = "last_evaluation_date")
    private LocalDate lastEvaluationDate;

    @Column(name = "completed_projects", columnDefinition = "int default 0")
    private int completedProjects;

    @Column(name = "total_review" , columnDefinition = "int default 0")
    private int totalReview;

    @Column(name = "ongoing_projects", columnDefinition = "int default 0")
    private int ongoingProjects;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", columnDefinition = "enum('active', 'inactive', 'suspended') default 'active'")
    private AccountStatus accountStatus;

    @Column(name = "language", columnDefinition = "varchar(255) default 'en'")
    private String language;
    @Column (name = "profile_picture", columnDefinition = "TEXT")
    private String profile_picture;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public enum AvailabilityStatus {
        available, busy, on_leave
    }

    public enum AccountStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }

    public enum ROLE {
        USER, ADMIN
    }

    // User to Posts
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();
    //  User to Comments
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("user")
    private List<Comment> comments;




    // User to Communities
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("createdBy")
    private List<Community> communities;

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<UserSkill> userSkills;
    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<CompanyEmployee> companyEmployees;
    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<CommunityUser> communityUsers;
    @OneToMany(mappedBy = "user1" , cascade = CascadeType.ALL ,orphanRemoval = true)
    private List<Connection> connectionInitiated;
    @OneToMany(mappedBy = "user2" , cascade = CascadeType.ALL ,orphanRemoval = true)
    private List<Connection> connectionReceived;

    //Evaluation relationships
    @OneToMany(mappedBy = "evaluatedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Evaluation> evaluationsGiven = new ArrayList<>();
    @OneToMany(mappedBy = "evaluatedTo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Evaluation> evaluationsReceived = new ArrayList<>();
    // project user creator
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects = new ArrayList<>();
    // project user
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectUser> projectUsers = new ArrayList<>();
    // project user hierarchy
    // User is superior in these hierarchies
    @OneToMany(mappedBy = "superiorUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectUserHierarchy> hierarchiesAsSuperior = new ArrayList<>();
    // User is subordinate in these hierarchies
    @OneToMany(mappedBy = "subordinateUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectUserHierarchy> hierarchiesAsSubordinate = new ArrayList<>();
    // task assigned to assigned by relationship
    @OneToMany(mappedBy = "assignedBy")
    private List<Tasks> tasksAssignedBy;

    @OneToMany(mappedBy = "assignedTo")
    private List<Tasks> tasksAssignedTo;
}
