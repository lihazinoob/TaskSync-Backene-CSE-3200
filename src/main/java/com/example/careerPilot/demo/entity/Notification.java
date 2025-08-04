package com.example.careerPilot.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "recipient_username", nullable = false)
    private String recipientUsername;

    @Column(name = "is_read", nullable = false)
    private boolean is_read = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}