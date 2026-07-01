package com.sanos.notificaciones.config;

import com.sanos.notificaciones.model.Notification;
import com.sanos.notificaciones.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerConfigTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private KafkaConsumerConfig kafkaConsumerConfig;

    @Test
    void escucharMascotas_callsSendNotification() {
        Notification mockNotification = new Notification(1L, "Alerta de Mascota: perro perdido", "sistema-alertas@sanos.com", null);
        when(notificationService.sendNotification(any(), any())).thenReturn(mockNotification);

        kafkaConsumerConfig.escucharMascotas("perro perdido");

        verify(notificationService, times(1)).sendNotification(
                eq("Alerta de Mascota: perro perdido"),
                eq("sistema-alertas@sanos.com")
        );
    }

    @Test
    void escucharMascotas_withEmptyMessage() {
        Notification mockNotification = new Notification(1L, "Alerta de Mascota: ", "sistema-alertas@sanos.com", null);
        when(notificationService.sendNotification(any(), any())).thenReturn(mockNotification);

        kafkaConsumerConfig.escucharMascotas("");

        verify(notificationService).sendNotification(
                eq("Alerta de Mascota: "),
                eq("sistema-alertas@sanos.com")
        );
    }
}
