package com.sanos.notificaciones.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanos.notificaciones.dto.NotificationDTO;
import com.sanos.notificaciones.factory.INotificationFactory;
import com.sanos.notificaciones.model.Notification;
import com.sanos.notificaciones.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
@AutoConfigureMockMvc(addFilters = false)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private INotificationFactory notificationFactory;

    @Autowired
    private ObjectMapper objectMapper;

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
        NotificationDTO dto = new NotificationDTO();
        dto.setMessage("Hola");
        dto.setRecipient("nicolas");

        Notification notification = createSampleNotification("Hola", "nicolas");

        when(notificationFactory.toEntity(any(NotificationDTO.class))).thenReturn(notification);
        when(notificationService.sendNotification("Hola", "nicolas")).thenReturn(notification);
        when(notificationFactory.toDTO(any(Notification.class))).thenReturn(dto);

        mockMvc.perform(post("/api/notificaciones/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Notificación procesada exitosamente"));

        verify(notificationService).sendNotification("Hola", "nicolas");
    }

    @Test
    void sendNotification_emptyBody_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/notificaciones/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendNotification_serviceException_returns500() throws Exception {
        NotificationDTO dto = new NotificationDTO();
        dto.setMessage("msg");
        dto.setRecipient("rcpt");

        Notification notification = createSampleNotification("msg", "rcpt");

        when(notificationFactory.toEntity(any(NotificationDTO.class))).thenReturn(notification);
        doThrow(new RuntimeException("kafka error"))
                .when(notificationService).sendNotification(anyString(), anyString());

        mockMvc.perform(post("/api/notificaciones/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError());
    }
}