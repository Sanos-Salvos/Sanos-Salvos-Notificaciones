package com.sanos.notificaciones.controller;

import com.sanos.notificaciones.dto.NotificationDTO;
import com.sanos.notificaciones.factory.INotificationFactory;
import com.sanos.notificaciones.model.Notification;
import com.sanos.notificaciones.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;
    private final INotificationFactory notificationFactory;

    public NotificationController(NotificationService notificationService, INotificationFactory notificationFactory) {
        this.notificationService = notificationService;
        this.notificationFactory = notificationFactory;
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> send(@RequestBody NotificationDTO notificationDTO) {
        try {
            Notification notificationEntity = notificationFactory.toEntity(notificationDTO);

            Notification processedNotification = notificationService.sendNotification(
                    notificationEntity.getMessage(),
                    notificationEntity.getRecipient()
            );

            NotificationDTO responseDTO = notificationFactory.toDTO(processedNotification);

            Map<String, Object> response = new HashMap<>();
            response.put("notificacion", responseDTO);
            response.put("mensaje", "Notificación procesada exitosamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}