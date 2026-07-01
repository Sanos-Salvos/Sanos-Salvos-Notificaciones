package com.sanos.notificaciones.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sanos.notificaciones.dto.NotificationDTO;
import com.sanos.notificaciones.factory.INotificationFactory;
import com.sanos.notificaciones.factory.NotificationFactoryImpl;
import com.sanos.notificaciones.model.Notification;
import com.sanos.notificaciones.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private NotificationService notificationService;

    @Mock
    private INotificationFactory notificationFactory;

    @InjectMocks
    private NotificationController notificationController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void send_returnsSuccess() throws Exception {
        NotificationDTO inputDTO = new NotificationDTO("Hola", "test@mail.com", LocalDateTime.now());
        Notification entity = new Notification(1L, "Hola", "test@mail.com", LocalDateTime.now());
        Notification saved = new Notification(1L, "Hola", "test@mail.com", LocalDateTime.now());
        NotificationDTO responseDTO = new NotificationDTO("Hola", "test@mail.com", saved.getTimestamp());

        when(notificationFactory.toEntity(any(NotificationDTO.class))).thenReturn(entity);
        when(notificationService.sendNotification(eq("Hola"), eq("test@mail.com"))).thenReturn(saved);
        when(notificationFactory.toDTO(any(Notification.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/notificaciones/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Notificación procesada exitosamente"))
                .andExpect(jsonPath("$.notificacion.message").value("Hola"))
                .andExpect(jsonPath("$.notificacion.recipient").value("test@mail.com"));
    }

    @Test
    void send_serviceThrows_returns500() throws Exception {
        NotificationDTO inputDTO = new NotificationDTO("msg", "r@mail.com", LocalDateTime.now());

        when(notificationFactory.toEntity(any(NotificationDTO.class)))
                .thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(post("/api/notificaciones/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Service error"));
    }

    @Test
    void send_invalidJson_returns400() throws Exception {
        mockMvc.perform(post("/api/notificaciones/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid json}"))
                .andExpect(status().isBadRequest());
    }
}
