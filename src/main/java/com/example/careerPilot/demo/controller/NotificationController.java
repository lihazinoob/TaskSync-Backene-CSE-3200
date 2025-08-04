package com.example.careerPilot.demo.controller;

import com.example.careerPilot.demo.dto.NotificationDTO;
import com.example.careerPilot.demo.entity.Notification;
import com.example.careerPilot.demo.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public List<NotificationDTO> getMyNotifications(@AuthenticationPrincipal UserDetails userDetails) {
        return notificationService.getNotificationsForUser(userDetails.getUsername());
    }

    @PutMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
    }
}