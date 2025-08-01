package com.example.careerPilot.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CommunityUser {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    public enum role{
        ADMIN,
        MEMBER,
        MODERATOR,
    }
    public enum status{
        ACCEPTED,
        PENDING,
        REJECTED
    }
    @Column(name = "status", columnDefinition = "VARCHAR(10)")
    @Enumerated(EnumType.STRING)
    private status status;

    @Column(name = "role", columnDefinition = "VARCHAR(10)")
    @Enumerated(EnumType.STRING)
    private role role;

    @CreationTimestamp
    private LocalDateTime joinDate;

}
