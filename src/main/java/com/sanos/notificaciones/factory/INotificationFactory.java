package com.sanos.notificaciones.factory;

import com.sanos.notificaciones.dto.NotificationDTO;
import com.sanos.notificaciones.model.Notification;

public interface INotificationFactory {
    NotificationDTO toDTO(Notification notification);
    Notification toEntity(NotificationDTO dto);
}