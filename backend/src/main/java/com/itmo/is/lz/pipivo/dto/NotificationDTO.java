package com.itmo.is.lz.pipivo.dto;

import com.itmo.is.lz.pipivo.model.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class NotificationDTO {

    private Long Id;
    private String message;
    private ProfileDTO user;
    private String status;

    public NotificationDTO(Notification notification) {
        this.Id = notification.getId();
        this.message = notification.getMessage();
        this.user = new ProfileDTO(notification.getUser());
        this.status = notification.getStatus();
    }
}
