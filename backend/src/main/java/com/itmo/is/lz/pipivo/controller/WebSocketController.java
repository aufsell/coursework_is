package com.itmo.is.lz.pipivo.controller;

import com.itmo.is.lz.pipivo.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebSocketController {

    private WebSocketService webSocketService;

    @MessageMapping("/getAllNotifications")
    public void getAllNotifications(Long userId) {
        webSocketService.sendAllNotifications(userId);
    }
}
