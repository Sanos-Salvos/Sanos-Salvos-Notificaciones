package com.sanos.notificaciones.service;

import com.sanos.notificaciones.model.Notification;
import com.sanos.notificaciones.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private NotificationService notificationService;

    private Notification savedNotification;

    @BeforeEach
    void setUp() {
        savedNotification = new Notification(1L, "Test msg", "test@mail.com", LocalDateTime.now());
    }

    @Test
    void sendNotification_savesAndSendsKafka() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(savedNotification);
        when(kafkaTemplate.send(eq("notifications"), any(String.class))).thenReturn(null);

        Notification result = notificationService.sendNotification("Test msg", "test@mail.com");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test msg", result.getMessage());
        assertEquals("test@mail.com", result.getRecipient());
        assertNotNull(result.getTimestamp());

        verify(notificationRepository, times(1)).save(any(Notification.class));
        verify(kafkaTemplate, times(1)).send("notifications", "Test msg");
    }

    @Test
    void sendNotification_setsTimestamp() {
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> {
            Notification n = invocation.getArgument(0);
            assertNotNull(n.getTimestamp());
            return savedNotification;
        });

        notificationService.sendNotification("msg", "r@mail.com");

        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void sendNotification_sendsCorrectKafkaMessage() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(savedNotification);
        when(kafkaTemplate.send(any(), any())).thenReturn(null);

        notificationService.sendNotification("Alerta urgente", "admin@sanos.com");

        verify(kafkaTemplate).send("notifications", "Alerta urgente");
    }

    @Test
    void sendNotification_repositoryThrows_doesNotSendKafka() {
        when(notificationRepository.save(any(Notification.class)))
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () ->
                notificationService.sendNotification("msg", "r@mail.com"));

        verify(kafkaTemplate, never()).send(any(), any());
    }
}
