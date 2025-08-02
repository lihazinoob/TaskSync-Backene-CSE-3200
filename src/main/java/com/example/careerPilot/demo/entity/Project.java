package com.example.careerPilot.demo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "Project"
)

public class Project
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;
    public enum ProjectStatus {
        IN_PROGRESS,
        COMPLETED,
        ON_HOLD
    }
    public enum ProjectRole {
        CREATOR,
        MEMBER
    }

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(name = "no_of_employees", nullable = false)
    private int noOfEmployees = 0;

    @Column(nullable = false)
    private BigDecimal completion = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_project_user"))
    private User createdBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status = ProjectStatus.IN_PROGRESS;

    @Column(name = "total_tasks")
    private int totalTasks = 0;

    @Column(name = "tasks_completed")
    private int tasksCompleted = 0;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "deadline")
    private LocalDateTime deadline;
}

