package com.sanos.notificaciones.config;

import com.sanos.notificaciones.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerConfig {

    private final NotificationService notificationService;

    public KafkaConsumerConfig(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "pet-topic", groupId = "notificaciones-group")
    public void escucharMascotas(String message) {
        System.out.println("Evento recibido desde PET: " + message);
        notificationService.sendNotification(
                "Alerta de Mascota: " + message,
                "sistema-alertas@sanos.com");
    }
}