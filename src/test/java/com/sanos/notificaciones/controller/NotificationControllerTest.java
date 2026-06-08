package com.sanos.notificaciones.controller;

import com.sanos.notificaciones.model.Notification;
import com.sanos.notificaciones.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
@AutoConfigureMockMvc(addFilters = false)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    private Notification createSampleNotification(String message, String recipient) {
        Notification n = new Notification();
        n.setId(1L);
        n.setMessage(message);
        n.setRecipient(recipient);
        n.setTimestamp(LocalDateTime.now());
        return n;
    }

    @Test
    void sendNotification_returns200() throws Exception {
        Notification notification = createSampleNotification("Hola", "nicolas");
        when(notificationService.sendNotification("Hola", "nicolas")).thenReturn(notification);

        mockMvc.perform(post("/api/notificaciones/send")
                        .param("message", "Hola")
                        .param("recipient", "nicolas"))
                .andExpect(status().isOk());

        verify(notificationService).sendNotification("Hola", "nicolas");
    }

    @Test
    void sendNotification_missingMessage_returns400() throws Exception {
        mockMvc.perform(post("/api/notificaciones/send")
                        .param("recipient", "nicolas"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendNotification_missingRecipient_returns400() throws Exception {
        mockMvc.perform(post("/api/notificaciones/send")
                        .param("message", "Hola"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendNotification_emptyParams_returns400() throws Exception {
        mockMvc.perform(post("/api/notificaciones/send"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendNotification_serviceException_returns500() throws Exception {
        doThrow(new RuntimeException("kafka error"))
                .when(notificationService).sendNotification(anyString(), anyString());

        mockMvc.perform(post("/api/notificaciones/send")
                        .param("message", "msg")
                        .param("recipient", "rcpt"))
                .andExpect(status().isInternalServerError());
    }
}
