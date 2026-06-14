package com.sanos.notificaciones.service;

import com.sanos.notificaciones.model.Notification;
import com.sanos.notificaciones.repository.NotificationRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public NotificationService(NotificationRepository notificationRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.notificationRepository = notificationRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Notification sendNotification(String message, String recipient) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setRecipient(recipient);
        notification.setTimestamp(LocalDateTime.now());

        Notification savedNotification = notificationRepository.save(notification);

        kafkaTemplate.send("notifications", message);

        return savedNotification;
    }
}