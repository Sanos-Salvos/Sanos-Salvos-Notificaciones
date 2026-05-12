package com.sanos.notificaciones.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sanos.notificaciones.model.Notification;
import com.sanos.notificaciones.service.NotificationService;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public Notification send(@RequestParam String message, @RequestParam String recipient) {
        return notificationService.sendNotification(message, recipient);
    }
}