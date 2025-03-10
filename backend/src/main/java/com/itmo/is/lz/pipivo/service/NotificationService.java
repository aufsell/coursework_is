package com.itmo.is.lz.pipivo.service;

import com.itmo.is.lz.pipivo.dto.NotificationDTO;
import com.itmo.is.lz.pipivo.dto.ProfileDTO;
import com.itmo.is.lz.pipivo.model.Notification;
import com.itmo.is.lz.pipivo.model.User;
import com.itmo.is.lz.pipivo.repository.NotificationRepository;
import com.itmo.is.lz.pipivo.repository.NotificationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final UserService userService;
    private final NotificationRepository notificationRepository;
    private final NotificationUserRepository notificationUserRepository;

    public List<NotificationDTO> getNotifications() {
        String username = userService.getCurrentUsername();
        User user = userService.getByUsername(username);
        List<Long> notificationIds = notificationUserRepository.getNotificationIdsByUserId(user.getId());
        if (notificationIds.isEmpty()) {
            return List.of();
        }
        List<Notification> notifications = notificationRepository.findAllById(notificationIds);
        return notifications.stream().map(this::convertToDTO).toList();
    }

    private NotificationDTO convertToDTO(Notification notification) {
        if (notification == null) {
            return null;
        }
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(notification.getId());
        notificationDTO.setUser(new ProfileDTO(notification.getUser()));
        notificationDTO.setMessage(notification.getMessage());
        return notificationDTO;

    }
}
