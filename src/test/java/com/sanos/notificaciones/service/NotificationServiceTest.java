package com.sanos.notificaciones.service;

import com.sanos.notificaciones.model.Notification;
import com.sanos.notificaciones.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    private static final String TOPIC = "notifications";

    @BeforeEach
    void setUp() {
        lenient().when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(inv -> {
                    Notification n = inv.getArgument(0);
                    n.setId(1L);
                    return n;
                });
    }

    @Test
    void sendNotification_savesAndSendsToKafka() {
        when(kafkaTemplate.send(eq(TOPIC), any(String.class)))
                .thenReturn(CompletableFuture.completedFuture(mock(SendResult.class)));

        Notification result = notificationService.sendNotification("Hola", "nicolas");

        assertNotNull(result);
        assertEquals("Hola", result.getMessage());
        assertEquals("nicolas", result.getRecipient());
        assertNotNull(result.getTimestamp());

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository).save(captor.capture());
        assertEquals("Hola", captor.getValue().getMessage());
        assertEquals("nicolas", captor.getValue().getRecipient());

        verify(kafkaTemplate).send(TOPIC, "Hola");
    }

    @Test
    void sendNotification_setsTimestampBeforeSave() {
        when(kafkaTemplate.send(anyString(), anyString()))
                .thenReturn(CompletableFuture.completedFuture(mock(SendResult.class)));

        Notification result = notificationService.sendNotification("test", "user");

        assertNotNull(result.getTimestamp());
        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository).save(captor.capture());
        assertNotNull(captor.getValue().getTimestamp());
    }

    @Test
    void sendNotification_callsKafkaAfterSave() {
        when(kafkaTemplate.send(anyString(), anyString()))
                .thenReturn(CompletableFuture.completedFuture(mock(SendResult.class)));

        notificationService.sendNotification("msg", "rcpt");

        var order = inOrder(notificationRepository, kafkaTemplate);
        order.verify(notificationRepository).save(any());
        order.verify(kafkaTemplate).send(anyString(), anyString());
    }

    @Test
    void sendNotification_kafkaFails_stillSaves() {
        when(kafkaTemplate.send(anyString(), anyString()))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("kafka down")));

        try {
            notificationService.sendNotification("msg", "rcpt");
        } catch (Exception ignored) {
        }

        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void sendNotification_nullMessage_handledGracefully() {
        when(kafkaTemplate.send(anyString(), any()))
                .thenReturn(CompletableFuture.completedFuture(mock(SendResult.class)));

        assertDoesNotThrow(() ->
                notificationService.sendNotification(null, "rcpt"));
    }
}