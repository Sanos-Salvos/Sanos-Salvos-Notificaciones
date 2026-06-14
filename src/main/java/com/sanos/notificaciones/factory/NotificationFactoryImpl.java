package com.sanos.notificaciones.factory;

import com.sanos.notificaciones.dto.NotificationDTO;
import com.sanos.notificaciones.model.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationFactoryImpl implements INotificationFactory {

    @Override
    public NotificationDTO toDTO(Notification notification) {
        if (notification == null) {
            return null;
        }

        return new NotificationDTO(
                notification.getMessage(),
                notification.getRecipient(),
                notification.getTimestamp()
        );
    }

    @Override
    public Notification toEntity(NotificationDTO dto) {
        if (dto == null) {
            return null;
        }

        Notification notification = new Notification();
        notification.setMessage(dto.getMessage());
        notification.setRecipient(dto.getRecipient());
        notification.setTimestamp(dto.getTimestamp());
        // El ID no se setea aquí porque la base de datos lo genera en el INSERT

        return notification;
    }
}