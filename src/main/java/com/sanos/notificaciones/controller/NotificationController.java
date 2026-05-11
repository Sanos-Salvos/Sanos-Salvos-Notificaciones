package com.sanos.notificaciones.controller;

import com.sanos.notificaciones.model.Notification;
import com.sanos.notificaciones.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public Notification send(@RequestParam String message, @RequestParam String recipient) {
        return notificationService.sendNotification(message, recipient);
    }
}