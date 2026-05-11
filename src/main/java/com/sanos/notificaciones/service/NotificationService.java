package com.sanos.notificaciones.service;

import com.sanos.notificaciones.model.Notification;
import com.sanos.notificaciones.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public Notification sendNotification(String message, String recipient) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setRecipient(recipient);
        notification.setTimestamp(LocalDateTime.now());
        notificationRepository.save(notification);
        kafkaTemplate.send("notifications", message);
        return notification;
    }
}