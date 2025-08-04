package com.example.careerPilot.demo.service;


import com.example.careerPilot.demo.dto.NotificationDTO;
import com.example.careerPilot.demo.entity.Notification;
import com.example.careerPilot.demo.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationDTO createNotification(String message, String recipientUsername) {
        Notification notification = Notification.builder()
                .message(message)
                .recipientUsername(recipientUsername)
                .createdAt(LocalDateTime.now())
                .build();
        Notification saved = notificationRepository.save(notification);

        messagingTemplate.convertAndSend("/topic/notifications/"+recipientUsername, notification);
        return toDTO(saved);
    }

    public List<NotificationDTO> getNotificationsForUser(String username) {
        return notificationRepository.findByRecipientUsernameOrderByCreatedAtDesc(username)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.set_read(true);
            notificationRepository.save(n);
        });
    }

    private NotificationDTO toDTO(Notification notification) {
        return new NotificationDTO(
                notification.getId(),
                notification.getMessage(),
                notification.getRecipientUsername(),
                notification.is_read(),
                notification.getCreatedAt()
        );
    }
}