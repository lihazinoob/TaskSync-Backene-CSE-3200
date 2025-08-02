package com.example.careerPilot.demo.entity;

// src/main/java/com/example/careerPilot/demo/entity/ProjectUser.java

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "project_user",
        uniqueConstraints = @UniqueConstraint(name = "unique_project_user", columnNames = {"project_id", "user_id"})
)
public class ProjectUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id", referencedColumnName = "projectId", nullable = false, foreignKey = @ForeignKey(name = "fk_projectuser_project"))
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_projectuser_user"))
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Project.ProjectRole role = Project.ProjectRole.MEMBER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    public enum RequestStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}