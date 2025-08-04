package com.example.careerPilot.demo.dto;


import com.example.careerPilot.demo.entity.Notification;
import com.example.careerPilot.demo.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private String message;
    private String recipientUsername;
    private boolean isread;
    private LocalDateTime createdAt;
}