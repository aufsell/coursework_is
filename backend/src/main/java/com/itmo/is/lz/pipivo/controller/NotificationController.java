package com.itmo.is.lz.pipivo.controller;

import com.itmo.is.lz.pipivo.dto.NotificationDTO;
import com.itmo.is.lz.pipivo.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity <List<NotificationDTO>> getNotifications() {
        List<NotificationDTO> notifications =  notificationService.getNotifications();
        return ResponseEntity.ok(notifications);
    }
}
