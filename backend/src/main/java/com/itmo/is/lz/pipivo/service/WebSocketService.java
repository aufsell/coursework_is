package com.itmo.is.lz.pipivo.service;

import com.itmo.is.lz.pipivo.model.Notification;
import com.itmo.is.lz.pipivo.repository.NotificationUserRepository;
import com.itmo.is.lz.pipivo.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private SimpMessagingTemplate messagingTemplate;

    private NotificationUserRepository notificationUserRepository;

    private NotificationRepository notificationRepository;

    public void sendNotification(Long userId, String message) {
        messagingTemplate.convertAndSendToUser(userId.toString(), "/topic/notifications", message);
    }

    public void sendAllNotifications(Long userId) {
        List<Long> notificationIds = notificationUserRepository.findByUserId(userId);

        if (notificationIds.isEmpty()) {
            return;
        }
        List<Notification> notifications = notificationRepository.findAllById(notificationIds);
        for (Notification notification : notifications) {
            if (notification != null) {
                sendNotification(userId, notification.getMessage());
            }
        }
    }
}
