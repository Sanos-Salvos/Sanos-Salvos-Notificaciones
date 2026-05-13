package com.sanos.notificaciones.config;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerConfig {

    @KafkaListener(topics = "pet-topic", groupId = "notificaciones-group")
    public void escucharMascotas(String message) {
        System.out.println("Evento recibido desde PET (notificaciones): " + message);
    }
}
